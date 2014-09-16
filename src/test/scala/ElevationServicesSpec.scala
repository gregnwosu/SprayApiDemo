package com.christophergagne.sprayapidemo

import org.scalatest.{FreeSpec, Matchers}
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest



class ElevationServiceSpec extends  FreeSpec with SprayApiDemoService with ScalatestRouteTest with Matchers {
  def actorRefFactory = system
   "The Timezone Service" - {
     "when calling GET /api/TimezoneService/39.-119/1331161200" - {
       "should return 'Pacific Standard Time'" in {
         Get ("/api/TimezoneService/39/-119/1331161200") ~>  sprayApiDemoRoute ~> check {
            status should equal (OK)
            entity.toString should include ("1159.288949429688")
         }
       }
     }
   }




}
