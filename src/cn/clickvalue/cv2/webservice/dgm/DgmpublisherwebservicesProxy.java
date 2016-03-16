package cn.clickvalue.cv2.webservice.dgm;

import java.util.List;


public class DgmpublisherwebservicesProxy implements cn.clickvalue.cv2.webservice.dgm.Dgmpublisherwebservices {
  private String _endpoint = null;
  private cn.clickvalue.cv2.webservice.dgm.Dgmpublisherwebservices dgmpublisherwebservices = null;
  
  public DgmpublisherwebservicesProxy() {
    _initDgmpublisherwebservicesProxy();
  }
  
  public DgmpublisherwebservicesProxy(String endpoint) {
    _endpoint = endpoint;
    _initDgmpublisherwebservicesProxy();
  }
  
  private void _initDgmpublisherwebservicesProxy() {
    try {
      dgmpublisherwebservices = (new cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesServiceLocator()).getDgmpublisherwebservicesCfc();
      if (dgmpublisherwebservices != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)dgmpublisherwebservices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)dgmpublisherwebservices)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (dgmpublisherwebservices != null)
      ((javax.xml.rpc.Stub)dgmpublisherwebservices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cn.clickvalue.cv2.webservice.dgm.Dgmpublisherwebservices getDgmpublisherwebservices() {
    if (dgmpublisherwebservices == null)
      _initDgmpublisherwebservicesProxy();
    return dgmpublisherwebservices;
  }
  
  public java.lang.String getCampaigns(java.lang.String username, java.lang.String password, java.lang.String approvalType) throws java.rmi.RemoteException, cn.clickvalue.cv2.webservice.cfc.CFCInvocationException{
    if (dgmpublisherwebservices == null)
      _initDgmpublisherwebservicesProxy();
    return dgmpublisherwebservices.getCampaigns(username, password, approvalType);
  }
  
  public java.lang.String getAdvertiserStats(java.lang.String username, java.lang.String password, java.lang.String fromDate, java.lang.String toDate) throws java.rmi.RemoteException, cn.clickvalue.cv2.webservice.cfc.CFCInvocationException{
    if (dgmpublisherwebservices == null)
      _initDgmpublisherwebservicesProxy();
    return dgmpublisherwebservices.getAdvertiserStats(username, password, fromDate, toDate);
  }
  
  public java.lang.String getSales(java.lang.String username, java.lang.String password, double campaignid, java.lang.String saleStatus, java.lang.String dateFilterType, java.lang.String fromDate, java.lang.String toDate) throws java.rmi.RemoteException, cn.clickvalue.cv2.webservice.cfc.CFCInvocationException{
	if (dgmpublisherwebservices == null)
      _initDgmpublisherwebservicesProxy();
    return dgmpublisherwebservices.getSales(username, password, campaignid, saleStatus, dateFilterType, fromDate, toDate);
  }
  
  
}