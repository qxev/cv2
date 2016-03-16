/**
 * Dgmpublisherwebservices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.clickvalue.cv2.webservice.dgm;

import java.util.List;

public interface Dgmpublisherwebservices extends java.rmi.Remote {
    public java.lang.String getCampaigns(java.lang.String username, java.lang.String password, java.lang.String approvalType) throws java.rmi.RemoteException, cn.clickvalue.cv2.webservice.cfc.CFCInvocationException;
    public java.lang.String getAdvertiserStats(java.lang.String username, java.lang.String password, java.lang.String fromDate, java.lang.String toDate) throws java.rmi.RemoteException, cn.clickvalue.cv2.webservice.cfc.CFCInvocationException;
    public java.lang.String getSales(java.lang.String username, java.lang.String password, double campaignid, java.lang.String saleStatus, java.lang.String dateFilterType, java.lang.String fromDate, java.lang.String toDate) throws java.rmi.RemoteException, cn.clickvalue.cv2.webservice.cfc.CFCInvocationException;
}
