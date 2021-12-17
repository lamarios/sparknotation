# How to perform release

Guide: https://itnext.io/publishing-artifact-to-maven-central-b160634e5268

## Generate GPG Key

Generate a key with a passph
```shell
gpg --full-generate-key
```

Publish the key
```shell
gpg --keyserver keyserver.ubuntu.com --send-keys ${KEY_ID}
```

```shell script
 mvn clean install
 mvn clean deploy -Pdist
```