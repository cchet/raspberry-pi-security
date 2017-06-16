public class IRSensor_HCSR501 implements IRSensorDevice {
  private GpioController gpioController;
  
  // ...
  
    @Override
    public void runDevice() {
        log.debug("Starting HCSR501 sensor");

        // Already running
        if (gpioController != null) {
            log.debug("Starting HCSR501 sensor failed. Already started");
            return;
        }

      // get instance of gpio controller
      gpioController = GpioFactory.getInstance();
    
      // set data pin to observe
      final GpioPinDigitalInput sensor = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_15, "hcsr501");
    
      // add listener to handle events
      sensor.addListener((GpioPinListenerDigital) event -> {
      log.debug("HCSR501 sensor at pin '{}' changed state to '{}'", event.getPin(), event.getState());

      // check state of pin
      if (PinState.HIGH.equals(event.getState())) {
        // ...      
      });
    
      log.debug("Registered sensor listener");
      log.debug("Started HCSR501 sensor");
    }
  
  // ...
  
}