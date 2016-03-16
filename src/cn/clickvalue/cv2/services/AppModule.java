package cn.clickvalue.cv2.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.internal.services.ResponseRenderer;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.AliasContribution;
import org.apache.tapestry5.services.ApplicationInitializer;
import org.apache.tapestry5.services.ApplicationInitializerFilter;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.slf4j.Logger;

import cn.clickvalue.cv2.common.bindings.AffiliateVerifiedFormatBindingFactory;
import cn.clickvalue.cv2.common.bindings.BannerTypeFormatBindingFactory;
import cn.clickvalue.cv2.common.bindings.CommissionRuleFormatBindingFactory;
import cn.clickvalue.cv2.common.bindings.DateFormatBindingFactory;
import cn.clickvalue.cv2.common.bindings.FormatBindingFactory;
import cn.clickvalue.cv2.common.bindings.GeneralStateBingingFactory;
import cn.clickvalue.cv2.common.bindings.PaidFormatBindingFactory;
import cn.clickvalue.cv2.common.bindings.StatusFormatBindingFactory;
import cn.clickvalue.cv2.common.transaction.BigDecimalTranslator;
import cn.clickvalue.cv2.common.transaction.DateTranslator;
import cn.clickvalue.cv2.common.util.CheckBoxItem;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.AffiliateCampaign;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.Report;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.services.util.EventAuthController;
import cn.clickvalue.cv2.services.util.PageAuthController;

public class AppModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(PageAuthController.class).withId("PageAuthController");
		binder.bind(EventAuthController.class).withId("EventAuthController");
		binder.bind(SupportedLocales.class, SupportedLocalesImpl.class);
	}

	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.PRODUCTION_MODE, "true");
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "zh,en");
		configuration.add("tapestry.start-page-name", "");
		configuration.add("tapestry.page-pool.soft-limit", "20");
		configuration.add("tapestry.page-pool.soft-wait", "150 ms");
		configuration.add("tapestry.page-pool.hard-limit", "100");
		configuration.add("tapestry.page-pool.active-window", "10 m");
	}

	public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration, BindingSource bindingSource) {
		configuration.add("format", new FormatBindingFactory(bindingSource));
		configuration.add("dateformat", new DateFormatBindingFactory(bindingSource));
		configuration.add("status", new StatusFormatBindingFactory(bindingSource));
		configuration.add("commissionruleformat", new CommissionRuleFormatBindingFactory(bindingSource));
		configuration.add("paid", new PaidFormatBindingFactory(bindingSource));
		configuration.add("generalstate", new GeneralStateBingingFactory(bindingSource));
		configuration.add("bannerTypeFormat", new BannerTypeFormatBindingFactory(bindingSource));
		configuration.add("affiliateVerified", new AffiliateVerifiedFormatBindingFactory(bindingSource));
	}

	// <input t:type="TextField" t:id="date" t:translate="translate:date" t:value="date" size="30" />
	public static void contributeTranslatorDefaultSource(MappedConfiguration<Class<?>, Translator<?>> configuration) {
		configuration.add(Date.class, new DateTranslator());
		configuration.add(BigDecimal.class, new BigDecimalTranslator());
	}

	public static void contributeTranslatorSource(MappedConfiguration<String, Translator<?>> configuration) {
		configuration.add("date", new DateTranslator());
		configuration.add("bigDecimal", new BigDecimalTranslator());
	}

	public void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration,
			@InjectService("EventAuthController") Dispatcher eventAuthController,
			@InjectService("PageAuthController") Dispatcher pageAuthController) {
		configuration.add("eventAuthController", eventAuthController, "before:ComponentEvent");
		configuration.add("PageAuthController", pageAuthController, "before:PageRender");
	}

	public static void contributeComponentClassTransformWorker(OrderedConfiguration<ComponentClassTransformWorker> configuration,
			PropertyAccess propertyAccess) {
		configuration.add("InjectSelectionModel", new InjectSelectionModelWorker(propertyAccess), "after:Inject*");
	}

	public static void contributeDefaultDataTypeAnalyzer(MappedConfiguration<Class<?>, String> configuration) {
		configuration.add(User.class, "user");
		configuration.add(List.class, "list");
		configuration.add(Campaign.class, "campaign");
		configuration.add(AffiliateCampaign.class, "affiliateCampaign");
		configuration.add(Banner.class, "banner");
		configuration.add(AffiliateCategory.class, "affiliateCategory");
		configuration.add(UserGroup.class, "userGroup");
		configuration.add(Site.class, "site");
		configuration.add(CheckBoxItem.class, "checkBoxItem");
		configuration.add(Account.class, "account");
		configuration.add(Report.class, "report");
	}

	/**
	 * 初始化网站的数据
	 * 
	 * @param configuration
	 *            配置
	 */
	public void contributeApplicationInitializer(OrderedConfiguration<ApplicationInitializerFilter> configuration) {

		ApplicationInitializerFilter clearCaches = new ApplicationInitializerFilter() {
			// @Override
			public void initializeApplication(Context context, ApplicationInitializer initializer) {
				// city 城市变量
				// FIXME Init....
				initializer.initializeApplication(context);
			}
		};
		configuration.add("initialDataBase", clearCaches);
	}

	/**
	 * cat 加入了 utf-8过滤器
	 * 
	 * @return HttpServletRequestFilter
	 */
	public HttpServletRequestFilter buildUtf8Filter() {
		return new HttpServletRequestFilter() {
			public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
					throws IOException {
				request.setCharacterEncoding("UTF-8");
				return handler.service(request, response);
			}
		};
	}

	// public PageResponseRenderer decoratePageResponseRenderer(
	// @InjectService("PageMarkupRenderer")
	// final PageMarkupRenderer markupRenderer,
	// @InjectService("MarkupWriterFactory")
	// final MarkupWriterFactory markupWriterFactory, final Object delegate) {
	// return new PageResponseRendererImpl() {
	// public void renderPageResponse(Page page) throws IOException {
	//				
	// }
	// };
	// }

	public void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration,
			@InjectService("Utf8Filter") HttpServletRequestFilter utf8Filter) {
		configuration.add("Utf8Filter", utf8Filter, "before:MultipartFilter");
	}

	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		// Creates a virtual root pacakge for pages,components.
		configuration.add(new LibraryMapping("man", "net.sf.lombok"));
	}

	public static void contributeAlias(Configuration<AliasContribution<?>> configuration) {
		// configuration.add(AliasContribution.create(contributionType, object))
		// configuration.add(AliasContribution.create(MarkupWriterFactory.class,
		// new XhtmlMarkupWriterFactoryImpl()));
	}

	/**
	 * Decorate Error page
	 * 
	 * @param logger
	 * @param renderer
	 * @param componentSource
	 * @param productionMode
	 * @param service
	 * @return
	 */
	public RequestExceptionHandler decorateRequestExceptionHandler(final Logger logger, final ResponseRenderer renderer,
			final ComponentSource componentSource, @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode, Object service) {
		if (!productionMode) {
			return null;
		}

		return new RequestExceptionHandler() {
			public void handleRequestException(Throwable exception) throws IOException {
				logger.error("Unexpected runtime exception: " + exception.getMessage(), exception);
				ExceptionReporter error = (ExceptionReporter) componentSource.getPage("Error");
				error.reportException(exception);
				renderer.renderPageMarkupResponse("Error");
			}
		};
	}
}
