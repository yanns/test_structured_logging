# Test structured logging on GCP

Test structure logging on GCP (Stackdriver & Kubernetes) with Scala

## Problem with "normal" logging

With a standard Kubernetes cluster on the Google Cloud platform, Stackdriver parses the stdout and stderr of each containers to ingest logs.
But this has several limitations:

- we can only use `INFO` (stdout) or `ERROR` (stderr) levels
- logs with a new line (like with a stacktrace) are parsed in different items (one per line).

## Solution proposed by Stackdriver

Using a special logback appender `com.google.cloud.logging.logback.LoggingAppender`:

https://cloud.google.com/logging/docs/setup/java#wzxhzdk57wzxhzdk58logback_appender_for_product_name

With a very simple configuration, we can use all different severities:

```
{
 labels: {…}
 resource: {…}
 severity: "INFO"
 textPayload: "hello info"
}

{
 labels: {…}
 resource: {…}
 severity: "WARNING"
 textPayload: "hello warn"
}
```


Stacktraces are handled the correct way:
```
{
 labels: {
  levelName: "ERROR"
  levelValue: "40000"
 }
 resource: {…}
 severity: "ERROR"
 textPayload: "there was an exception
java.lang.Exception: boom!!
    at example.Hello$.willBreak(Hello.scala:9)
    at example.Hello$.main(Hello.scala:16)
    at example.Hello.main(Hello.scala)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:564)
    at sbt.Run.invokeMain(Run.scala:93)
    at sbt.Run.run0(Run.scala:87)
    at sbt.Run.execute$1(Run.scala:65)
    at sbt.Run.$anonfun$run$4(Run.scala:77)
    at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:12)
    at sbt.util.InterfaceUtil$$anon$1.get(InterfaceUtil.scala:10)
    at sbt.TrapExit$App.run(TrapExit.scala:252)
    at java.base/java.lang.Thread.run(Thread.java:844)"
}
```

### Logging enhancement

Using a [LoggingEnhancer](src/main/scala/example/ExampleLoggingEnhancer.scala), it's possible to add custom key/values:

```
 {
 labels: {
  levelName: "INFO"
  levelValue: "20000"
  my-key: "my value"
 }
 resource: {…}
 severity: "INFO"
 textPayload: "hello info"
}
```


### Limitations

- duplicated log entries

If we use only the special logback appender for google cloud, then we have no logs on stdout/stderr.
But if we also use stdout/stderr, then the logs are ingested 2 times, leading to duplicated log entries. ;(

- library in beta - some crucial fields are missing

Some crucial information for the environment are missing:
```
resource: {
  labels: {
   cluster_name: ""
   container_name: ""
   instance_id: ""
   namespace_id: ""
   pod_id: ""
   project_id: "<project ID>"
   zone: "<zone>"
  }
  type: "container"
 }
```

https://github.com/GoogleCloudPlatform/google-cloud-java/issues/2912
