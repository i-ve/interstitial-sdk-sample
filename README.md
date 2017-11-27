# interstitial-sdk-sample
아이브 전면 광고 SDK 샘플 프로젝트

## 1. 프로젝트 설정
### 1-1. 프로젝트의 gradle.properties 파일의 repositories에 다음의 maven 저장소를 추가합니다.
```groovy
maven { url "https://raw.githubusercontent.com/i-ve/interstitial-sdk-mvn-repo/master/releases" }
```

최종 적용된 코드는 다음과 비슷하게 될 것 입니다.
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://raw.githubusercontent.com/i-ve/interstitial-sdk-mvn-repo/master/releases" }
    }
}
```
주의 : 하나의 프로젝트에는 보통 2개 이상의 gradle.properties 파일이 있는데, 이 중 *모듈이 아닌 프로젝트*의 gradle.properties 파일을 수정해야합니다.

### 1-2. 모듈의 gradle.properties 파일의 dependencies에 다음 내용을 추가합니다.

```groovy
compile 'kr.ive:interstitial_sdk:1.0.4'
```
gradle 파일을 수정하게 되면 Android Studio에서 `Sync Now`버튼이 보이게 됩니다. 
Sync를 하게 되면 메이븐 저장소에서 전면 광고 SDK 라이브러리를 다운로드 받게 됩니다.(로컬 저장소에 다운받기 때문에 프로젝트에서는 볼 수 없습니다)

### 1-3. AndroidManifest.xml 파일의 application안에 다음 내용을 추가합니다.
```xml
<meta-data android:name="ive_sdk_appcode" android:value="TIdKKXBq9C" />
<meta-data android:name="ive_interstitial_sdk_join_button_color" android:resource="@color/colorPrimary" />
<meta-data android:name="ive_interstitial_sdk_join_button_text_color" android:resource="@android:color/white" />
```
* `ive_sdk_app_code`의 값에는 아이브에서 발급받은 앱코드를 삽입합니다. 위에서 사용하고 있는 값은 테스트를 위한 용도입니다.
* `ive_interstitial_sdk_join_button_color`의 값에는 광고 참여 버튼의 배경 색상으로 사용할 color 리소스를 설정합니다.
* `ive_interstitial_sdk_join_button_text_color`의 값에는 광고 참여 버튼의 텍스트 색상으로 사용할 color 리소스를 설정합니다.

## 2. 전면광고 SDK 사용하기
### 2-1. SDK 초기화
다음의 초기화 코드는 애플리케이션이 실행될 때 단 한번만 호출합니다.
```java
IveAd.getInstance().initialize(activity);
```
### 2-2. 전면 광고 열기
광고가 있을 경우 전면 광고가 노출되고, 없을 경우에는 아무 것도 나오지 않습니다.
```java
IveAd.getInstance().showInterstitialAd(activity);
```
### 2-3. 종료 다이얼로그 열기

광고가 있을 경우에는 광고를 포함하는 종료 다이얼로그가 나오고, 광고가 없을 경우에는 앱이 바로 종료됩니다.
```java
IveAd.getInstance().showFinishAd(activity);
```

## 3. Trouble Shooting

### 3-1. support 라이브러리를 찾을 수 없는 경우

```groovy
Failed to resolve: com.android.support:appcompat-v7:25.4.0
Install Repository and sync project
Show in File
Show in Project Structure dialog
```

gradle sync 중에 위와 비슷한 메시지를 보게 된다면, 프로젝트의 `build.gradle` 파일의 `repositories`에 다음 내용을 추가합니다.

```groovy
maven { url "https://maven.google.com" }
```

### 3-2. support 라이브러리의 버전 문제

프로젝트의 `build.gradle` 파일에 `dependencies` 부분에 특정 support 라이브러리에 빨간 밑줄이 뜨며, 마우스를 가져가면 다음과 같은 메시지가 뜨는 경우가 발생할 수 있습니다.

```groovy
All com.android.support libraries must use the exact same version specification (mixing versions can lead to runtime crashes). Found versions 27.0.0, 25.3.1. Examples include com.android.support:animated-vector-drawable:27.0.0 and com.android.support:design:25.3.1 less... (⌘F1) 
There are some combinations of libraries, or tools and libraries, that are incompatible, or can lead to bugs. One such incompatibility is compiling with a version of the Android support libraries that is not the latest version (or in particular, a version lower than your targetSdkVersion.)
```

이는 모든 support 라이브러리가 같은 버전을 사용해야 함을 의미합니다.

이 문제를 해결하기 위해서는 먼저 디펜던시에 대한 트리를 확인해야 합니다.

Android Studio에 있는 Terminal에서 다음 명령을 입력합니다.

```shell
./gradlew -q dependencies app:dependencies --configuration compile
```

위 명령 중 app은 프로젝트의 모듈명이 되어야 합니다.

위 명령의 결과로 터미널 상에 디펜던시 트리를 볼 수 있고, 그 중 일부만 살펴보겠습니다.

```shell
 +--- com.android.support:appcompat-v7:25.3.1 -> 27.0.0 (*)
 \--- com.android.support:design:25.3.1
```

`com.android.support:appcompat-v7`의 경우는 `25.3.1` 버전이었는데 `27.0.0`을 사용하도록 자동 변경되었는데, `com.android.support:design`의 경우는 `25.3.1`을 그대로 사용하고 있습니다.

이로 인해서 2가지(`25.3.1`과 `27.0.0`) 버전의 support 라이브러리가 혼재돼 있는 문제가 발생한 것이므로 이를 해결해주기 위해서는 `build.gradle`의 `dependencies`에 위 결과 트리에서 낮은 버전으로 표기돼 있는 라이브러리들을 모두 추가해서 명시적으로 높은 버전을 적어 줍니다.

이 경우에는 다음 내용을 `dependencies`에 추가하면 됩니다.

```groovy
compile 'com.android.support:design:27.0.0'
```

필요한 디펜던시를 모두 명시적으로 추가한 뒤에 gradle sync를 수행하면 이 문제를 해결할 수 있습니다.

## 4. SDK 변경 이력

#### v 1.0.4

* orientation 변경 시 발생하던 버그 수정

#### v 1.0.3

* 광고 참여 버튼 간격 및 크기 변경 