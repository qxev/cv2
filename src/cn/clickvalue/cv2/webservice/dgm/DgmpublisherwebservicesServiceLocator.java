/**
 * DgmpublisherwebservicesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.clickvalue.cv2.webservice.dgm;

public class DgmpublisherwebservicesServiceLocator extends org.apache.axis.client.Service implements cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesService {

    public DgmpublisherwebservicesServiceLocator() {
    }


    public DgmpublisherwebservicesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DgmpublisherwebservicesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DgmpublisherwebservicesCfc
    private java.lang.String DgmpublisherwebservicesCfc_address = "http://webservices.dgmpro.com/dgmpublisherwebservices.cfc";

    public java.lang.String getDgmpublisherwebservicesCfcAddress() {
        return DgmpublisherwebservicesCfc_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DgmpublisherwebservicesCfcWSDDServiceName = "dgmpublisherwebservices.cfc";

    public java.lang.String getDgmpublisherwebservicesCfcWSDDServiceName() {
        return DgmpublisherwebservicesCfcWSDDServiceName;
    }

    public void setDgmpublisherwebservicesCfcWSDDServiceName(java.lang.String name) {
        DgmpublisherwebservicesCfcWSDDServiceName = name;
    }

    public cn.clickvalue.cv2.webservice.dgm.Dgmpublisherwebservices getDgmpublisherwebservicesCfc() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DgmpublisherwebservicesCfc_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDgmpublisherwebservicesCfc(endpoint);
    }

    public cn.clickvalue.cv2.webservice.dgm.Dgmpublisherwebservices getDgmpublisherwebservicesCfc(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesCfcSoapBindingStub _stub = new cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesCfcSoapBindingStub(portAddress, this);
            _stub.setPortName(getDgmpublisherwebservicesCfcWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDgmpublisherwebservicesCfcEndpointAddress(java.lang.String address) {
        DgmpublisherwebservicesCfc_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.clickvalue.cv2.webservice.dgm.Dgmpublisherwebservices.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesCfcSoapBindingStub _stub = new cn.clickvalue.cv2.webservice.dgm.DgmpublisherwebservicesCfcSoapBindingStub(new java.net.URL(DgmpublisherwebservicesCfc_address), this);
                _stub.setPortName(getDgmpublisherwebservicesCfcWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("dgmpublisherwebservices.cfc".equals(inputPortName)) {
            return getDgmpublisherwebservicesCfc();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://DefaultNamespace", "dgmpublisherwebservicesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://DefaultNamespace", "dgmpublisherwebservices.cfc"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DgmpublisherwebservicesCfc".equals(portName)) {
            setDgmpublisherwebservicesCfcEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
