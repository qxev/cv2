package cn.clickvalue.cv2.test;

public interface IComputeArea {

    /*
     * Compute the area of obj
     */
    public double compute(Object obj);

    // public static IComputeArea buildAreaCalculator(
    // Map<Class, IComputeArea> configuration,
    // @InjectService("StrategyBuilder")
    // StrategyBuilder builder) {
    // StrategyRegistry<IComputeArea> registry = StrategyRegistry.newInstance(
    // IComputeArea.class, configuration);
    //
    // return builder.build(registry);
    // }
    //
    // public static void contributeAreaCalculator(
    // MappedConfiguration<Class, IComputeArea> configuration) {
    // configuration.add(Circle.class, new CircleArea());
    // configuration.add(Square.class, new SquareArea());
    // }

}
