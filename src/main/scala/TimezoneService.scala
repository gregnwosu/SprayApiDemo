package com.christophergagne.sprayapidemo

import akka.actor.Actor
import akka.event.Logging
import spray.client.pipelining._
import spray.routing.RequestContext

import scala.util.{Failure, Success}

object TimezoneService {
  case class Process (long:Double, lat: Double, timestamp: String)


}


class TimezoneService (requestContext: RequestContext) extends Actor {
  import com.christophergagne.sprayapidemo.TimezoneService._

  implicit val system = context.system

  import system.dispatcher

  val log = Logging (system, getClass)

  def receive = {
    case Process (long, lat, timestamp) =>
      process (long,lat,timestamp)
      context.stop (self)

  }


  def process (long:Double, lat:Double, timestamp:String) = {

    log.info ("Requesting timezone long: {} , lat : {} , timestamp : {}", long, lat , timestamp)

    import com.christophergagne.sprayapidemo.TimezoneJsonProtocol._
    import spray.httpx.SprayJsonSupport._

    val pipeline = sendReceive ~> unmarshal [GoogleTimezoneApiResult [Timezone]]
    val responseFuture = pipeline {
      Get(s"https://maps.googleapis.com/maps/api/timezone/json?location=$long,$lat&timestamp&sensor=false")
    }
    responseFuture onComplete {
      case Success (GoogleTimezoneApiResult (_, _, timeZoneName)) =>
        log.info ("The timezone is : {} m ", timeZoneName)
        requestContext.complete (timeZoneName)
      case Failure (error) =>
        requestContext.complete (error)
    }
  }









}
