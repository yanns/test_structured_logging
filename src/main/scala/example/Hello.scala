package example

import com.typesafe.scalalogging.StrictLogging


object Hello extends StrictLogging {

  def willBreak(): Unit = {
    throw new Exception("boom!!")
  }

  def main(args: Array[String]): Unit = {
    logger.error("hello error")

    try {
      willBreak()
    } catch {
      case t: Throwable ⇒ logger.error("there was an exception", t)
    }

    logger.warn("hello warn")
    logger.info("hello info")
    logger.info("hello info")
    logger.debug("hello debug")
    println("done")
  }

}

