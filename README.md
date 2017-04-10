A sample app to explain a request control with RxJava


# What this sample does
This sample app does simple refresh request. There are 3 types of refreshing;

### Regular refresh request
- It will request refresh anytime

### Lazy refresh request
- It will request refresh after 5 seconds passed
- It will be canceled once other refresh request occurs
- It uses `delay` operator which kicks PublishProcessor once 5 seconds has passed

### Triggers refresh request
- If previous request has been done in recent 10 second, it will cancel request; otherwise request the refresh
- It uses PublishProcessor with `throttleFirst` operator
