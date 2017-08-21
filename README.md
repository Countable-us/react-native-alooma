
#### iOS

- Add alooma to your Podfile with `pod "Alooma-iOS"`

#### Android

- Your app must be compiled with google play services
- Add `<uses-permission android:name="android.permission.INTERNET" /> `.
  More info: <uses-permission android:name="android.permission.INTERNET" /> 

### Linking

`react-native link react-native-alooma


## Usage
```javascript
import Alooma from 'react-native-alooma';

// TODO: What to do with the module?
Alooma.sharedInstanceWithToken("Your token")
Alooma.track('an event')
```
  
