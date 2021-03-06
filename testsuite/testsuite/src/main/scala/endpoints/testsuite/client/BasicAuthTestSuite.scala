package endpoints.testsuite.client

import com.github.tomakehurst.wiremock.client.WireMock._
import endpoints.algebra.BasicAuthentication
import endpoints.testsuite.BasicAuthTestApi

trait BasicAuthTestSuite[T <: BasicAuthTestApi] extends ClientTestBase[T] {

  def basicAuthSuite() = {

    "Client interpreter" should {

      "authenticate with given credentials" in {

        val credentials = BasicAuthentication.Credentials("user1", "pass2")
        val response = "wiremockeResponse"

        wireMockServer.stubFor(get(urlEqualTo("/users"))
          .withBasicAuth(credentials.username, credentials.password)
          .willReturn(aResponse()
            .withStatus(200)
            .withBody(response)))

        whenReady(call(client.protectedEndpoint, credentials))(_ shouldEqual Some(response))

      }

      "return None is authentication failed" in {

        val credentials = BasicAuthentication.Credentials("user1", "pass2")

        wireMockServer.stubFor(get(urlEqualTo("/users"))
          .willReturn(aResponse()
            .withStatus(403)
            .withBody("")))

        whenReady(call(client.protectedEndpoint, credentials))(_ shouldEqual None)

      }
    }
  }
}
