# Java Implementation of Opus
This is a fork of [Oggus](https://github.com/leonfancy/oggus), backported to Java 8, removed dependencies, with JavaSound SPI support.

This is a Java library for reading and writing Ogg Opus stream. Opus packet structure is supported.

For more description about Ogg Opus, please refer to following links:
- [RFC3533: The Ogg Encapsulation Format](https://tools.ietf.org/html/rfc3533)
- [Ogg bitstream overview](https://xiph.org/ogg/doc/oggstream.html)
- [RFC6716: Definition of the Opus Audio Codec](https://tools.ietf.org/html/rfc6716#section-3.1).
- [RFC7845: Ogg Encapsulation for the Opus Audio Codec](https://tools.ietf.org/html/rfc7845).

## Add the library to your project (gradle)
1. Add the Maven Central repository (if not exist) to your build file:
```groovy
repositories {
    ...
    mavenCentral()
}
```

2. Add the dependency:
```groovy
dependencies {
    ...
    implementation 'com.tianscar.javasound:javasound-opus:1.2.1'
}
```

## Usage
[Tests and Examples](/src/test/java/org/chenliang/oggus/test)

Note you need to download test audios [here](https://github.com/Tianscar/fbodemo1) and put them to /src/test/java/resources to run the test code properly!

## License
[MIT](/LICENSE)
