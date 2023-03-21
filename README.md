# QRHunter

A CMPUT301 android app for a Pokemon-Go-like game! Players can scan QR Codes and compete with other players.


## Getting Started
To run the project locally, begin by cloning the repository:

    git clone git@https://github.com/CMPUT301W23T14/QRHunter-App

Then create a `local.properties` file under the **QRHunter** directory following the `local.properties.example`. 

## Dependencies
|Name  |Uses  |
|--|--|
|Material3  | This project follows Google's [Material 3](https://m3.material.io/) design system|
|Firebase | This project uses Google's Firebase Cloud [Firestore](https://firebase.google.com/docs/firestore) for the database|
|Android Jetpack: Navigation  | The app uses [Android's Jetpack Navigation Component](https://developer.android.com/guide/navigation) for implementing single-activity architecture and handling navigation  |
|Android Jetpack's View Binding| The app uses [Android's Jetpack View Binding](https://developer.android.com/topic/libraries/view-binding) to make interacting with views easier  |
|JUnit | Unit and Intent tests |
|[yuriy-budiyev/code-scanner](https://developer.android.com/guide/navigation)| For scanning QR Codes |
|[Guava](https://github.com/google/guava)| For hashing the content of the QR Code |
|[RXBinding](https://github.com/JakeWharton/RxBinding)| Used for input debounce |
|Google maps| For displaying map and markers |


## App Architecture
![App Architecture Diagram](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview.png)
[Source](https://developer.android.com/topic/architecture#recommended-app-arch)

This project loosely follow Android’s official [Guide to App Architecture](https://developer.android.com/topic/architecture) (MVVM). The [Domain Layer](https://developer.android.com/topic/architecture/domain-layer) is not implemented in this project. 

### UI Layer
The recommended [**single-activity architecture**](https://www.youtube.com/watch?v=2k8x8V77CrU)  and [**ViewModel**](https://developer.android.com/topic/libraries/architecture/viewmodel) classes are used which makes navigation easier using Jetpack's Navigation Component. Android Jetpack's [**View Binding**](https://developer.android.com/topic/libraries/view-binding) is also used to mostly replace `findViewById`.

Anything related to the UI of the project goes under `ui` package in the app. This package include adapters, Fragments and ViewModels. Each screen (the Fragment and its ViewModel) in the app goes under the `ui` package. *Example: A profile page would be a `profile` folder containing `ProfileFragment.java` and `ProfileViewModel.java`.*

### Domain Layer
The [recommended convention](https://developer.android.com/topic/architecture/domain-layer#conventions) for the Domain Layer is not followed. Instead of a single class that is responsible for a single action, we have a `utils` package that contains a utility class for their respective model class. *Example: `QRCode.java` would have a `QRCodeUtils.java`*. These utility classes holds a collection of static methods that are reused and unrelated to the UI and application data, such as: `generateQRCodeHash()`

### Data Layer  
Following Android's official recommendation, application data in the database is exposed to the UI using **repository classes** so that any UI component such as Activities, Fragments and ViewModels would never interact with the Data Source. 

Anything related to the data goes under the `data` package in the app. This package contains **model classes** and **repository classes**. *Note: All the repository classes (`PlayerRepository.java` and `QRCodeRepository.java`*) extends from `DataRepository.java` that holds a static reference to the Firestore database. 

## Other Documentations
Other documentations are available in the [wiki](https://github.com/CMPUT301W23T14/QRHunter-App/wiki). 

