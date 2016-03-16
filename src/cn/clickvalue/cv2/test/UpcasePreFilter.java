package cn.clickvalue.cv2.test;

public class UpcasePreFilter implements StringTransformFilter {
    public String transform(String input, StringTransformService delegate) {
        String preProcess = input.toUpperCase();
        return delegate.transform(preProcess);
    }
}

// Alternately, the filter could pass input to delegate unchanged, but invoke
// toUpperCase() on the result:
// public class UpcasePostFilter implements StringTransformFilter
// {
// public String transform(String input, StringTransformService delegate)
// {
// return delegate.transform(input).toUpperCase();
// }
// }

// public void MyFilterImpl implements MyFilter
// {
// public void myMethod(int arg1, String arg2, MyService service)
// {
// // Do something before ...
//
// service.myMethod(arg1, arg2);
//
// // Do something after ...
// }
// }

// It's a lot like interceptors; the filter can do work before and after the
// service (which is really the next filter in the chain, in disguise) is
// invoked. It can also pass different or modified parameters in, and modify the
// result. Bargain basement AOP.
