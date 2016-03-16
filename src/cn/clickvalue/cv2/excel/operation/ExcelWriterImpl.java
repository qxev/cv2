package cn.clickvalue.cv2.excel.operation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import cn.clickvalue.cv2.common.util.AccessorUtil;
import cn.clickvalue.cv2.excel.config.model.ExcelConfigModel;
import cn.clickvalue.cv2.excel.config.model.PropertyModel;
import cn.clickvalue.cv2.excel.parseXml.ParseXmlService;
import cn.clickvalue.cv2.excel.parseXml.ParseXmlServiceImpl;

public class ExcelWriterImpl implements ExcelWriter {
    
    public String destFolderForUser(Integer userId) {
        StringBuffer sb = new StringBuffer("user_");
        sb.append(userId);
        sb.append("_excel");
        return sb.toString();    
    }
    
    public WritableSheet  createExcel(WritableSheet ws, Class clazz, List modelList) {
        ParseXmlService parseXmlServiceImpl = new ParseXmlServiceImpl();
        ExcelConfigModel model = parseXmlServiceImpl.loadExcelConfigModel(clazz);
        List<PropertyModel> propertyModelList = model.getPropertyModelList();
        
        try {
            this.setExcelHead(ws, propertyModelList);
            this.setExcelBody(ws, propertyModelList, modelList);
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return ws;
    }
    
    private void setExcelHead(WritableSheet ws, List<PropertyModel> propertyModelList) throws RowsExceededException, WriteException {
        for(int col=0;col<propertyModelList.size(); col++) {
            PropertyModel propertyModel = propertyModelList.get(col);
            Label label = new Label(col,0,propertyModel.getExcelTitleName());
            ws.addCell(label);
        }
    }
    
    private void setExcelBody(WritableSheet ws, List<PropertyModel> propertyModelList, List  modelList) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, RowsExceededException, WriteException {
        /**
         * 循环需要写入Excel的集合
         */
        for (int i = 0; i < modelList.size(); i++) {
            /**
             * 取出表头列数
             */
            int columns = ws.getColumns();
            /**
             * 取出每一个需要写入Excel的对象
             */
            Object object = (Object) modelList.get(i);
            /**
             * 循环所有列
             */
            for (int col = 0; col < columns; col++) {
                /**
                 * 取出每一个列的名称
                 */
                String colNum = ws.getCell(col, 0).getContents();
                /**
                 * 循环映射集合
                 */
                for (int mappings = 0; mappings < propertyModelList.size(); mappings++) {

                    PropertyModel propertyModel =  propertyModelList.get(mappings);
                    /**
                     * Excel中列名与映射文件中配置的Excel列名比对
                     */
                    if (colNum.equals(propertyModel.getExcelTitleName())) {
                        
                        /**
                         * 获得Excel中列名相对应的类的属性的get方法
                         */
                        String getMethod = AccessorUtil.buildGetter(propertyModel.getName());
                        /**
                         * 取得object类中的get方法
                         */
                        Method method = object.getClass().getMethod(getMethod,
                                new Class[0]);
                        /**
                         * 执行object类中的get方法并取得返回值,toString()会空指针
                         */
                        String popValue = String.valueOf(method.invoke(object, new Object[0])
                                );
                        /**
                         * 将返回值写入对应的Excel列名下
                         */
                        Label label = new Label(col, i + 1, popValue);
                        ws.addCell(label);

                        break;

                    }

                }
            }
        }
    }
    }

