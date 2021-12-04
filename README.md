[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/nort3x/boot4JDA.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/nort3x/boot4JDA/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/nort3x/boot4JDA.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/nort3x/boot4JDA/alerts/)
[![](https://jitpack.io/v/nort3x/boot4jda.svg)](https://jitpack.io/#nort3x/boot4jda)
# boot4jda

## features
+ easy integration with spring-boot
+ supports everything JDA supports
+ supports multi-bot
+ supports intercommunication within bots
+ easy configuration

## Install


### maven

Step 1. Add the JitPack repository to your build file
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Step 2. Add the dependency
```xml
...
<dependency>
    <groupId>com.github.nort3x</groupId>
    <artifactId>boot4jda</artifactId>
    <version>1.0.2</version>
</dependency>
<dependency>
    <groupId>net.dv8tion</groupId>
    <artifactId>JDA</artifactId>
    <version>5.0.0-alpha.1</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>2.5.6</version>
</dependency>
```


### gradle

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Step 2. Add the dependency
```gradle
dependencies {
        implementation 'com.github.nort3x:boot4jda:1.0.0'
        implementation 'net.dv8tion:JDA:5.0.0-alpha.1' // your version
        implementation 'org.springframework.boot:spring-boot-starter:2.5.6' // your version
}
```

## Usage

## Roadmap
+ developing an actual bot with this
+ adding wrappers around popular features for easier configuration
+ adding more third-party libraries if needed

## Donation
if you liked **B4J** you can buy me a coffee [here](https://github.com/nort3x/nort3x/tree/main/donate) as a token of apperiation.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update Tests and Docs as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
