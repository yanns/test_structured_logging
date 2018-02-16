# test_structured_logging
Test structure logging on GCP (Stackdriver) with Scala

## Problem with "normal" logging

Stackdriver parses the stdout and stderr to ingest logs. But this has several limitations:

- we can only have `INFO` (stdout) or `ERROR` (stderr) levels
- logs with a new line (like with a stacktrace) are parsed in different items (one per line).

## Solution proposed by Stackdriver

Using a special logback appender:

https://cloud.google.com/logging/docs/setup/java#wzxhzdk57wzxhzdk58logback_appender_for_product_name

With a very simple configuration, we can use all different severities:

![Example of structure logs][structured logs.png]

Stacktraces are handled the correct way:
```
{
 insertId: "1cu73vjg12p4dtx"
 labels: {
  levelName: "ERROR"
  levelValue: "40000"
 }
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
