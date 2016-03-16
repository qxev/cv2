//package cn.clickvalue.cv2.components;
//
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.tapestry5.annotations.ApplicationState;
//import org.apache.tapestry5.annotations.Component;
//import org.apache.tapestry5.annotations.Persist;
//import org.apache.tapestry5.corelib.components.Form;
//import org.apache.tapestry5.corelib.components.PasswordField;
//import org.apache.tapestry5.ioc.annotations.Inject;
//
//import cn.clickvalue.cv2.model.User;
//import cn.clickvalue.cv2.pages.Start;
//
//public class Login {
//
//    @Component
//    private Form loginForm;
//
//    @ApplicationState
//    private User user;
//
//    @Component(id = "password")
//    private PasswordField passwordField;
//
//    @Persist
//    private String userName;
//
//    @Persist
//    private String password;
//
//    @Inject
//    private IUserModule userModule;
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    /**
//     * <pre>
//     * 处理用户登录验证，根据用户名从数据库中取到此用户信息，判断提交的密码SHA1值是否等于数据库中的用户密码。
//     * 如果验证通过
//     *      按用户所属组别进入用户的个人首页
//     * 否则
//     *      重新返回此登录页面，并提示用户登录信息错误
//     * </pre>
//     * 
//     * @throws UnsupportedEncodingException
//     * @throws NoSuchAlgorithmException
//     * @return nextPage
//     */
//    Object onSubmitFromLoginForm() throws NoSuchAlgorithmException,
//            UnsupportedEncodingException {
//        Class nextPage = null;
//        User authenticatedUser = null;
//        authenticatedUser = userModule.authenticate(userName, password);
//        if (authenticatedUser != null) {
//            this.user = authenticatedUser;
//            nextPage = Start.class;
//        }
//        return nextPage;
//    }
//    
//    List<String> onProvideCompletionsFromUserName(String partial) {
//        List<String> result = new ArrayList<String>();
//        result.add("tesst1");
//        result.add("tesst11");
//        result.add("tesst111");
//        return result;
//    }
//
//}
