# test_structured_logging
Test structure logging on GCP (Stackdriver) with Scala

## Problem with "normal" logging

Stackdriver parses the stdout and stderr to ingest logs. But this has several limitations:

- we can only have `INFO` (stdout) or `ERROR` (stderr) levels
- logs with a new line (like with a stacktrace) are parsed in different items (one per line).

## Solution proposed by Stackdriver

Using a special logback appender:

https://cloud.google.com/logging/docs/setup/java#wzxhzdk57wzxhzdk58logback_appender_for_product_name

