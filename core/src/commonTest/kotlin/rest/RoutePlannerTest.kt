package rest

import RestTestLavakord
import TestNode
import Tests.runBlocking
import dev.schlaubi.lavakord.NoRoutePlannerException
import dev.schlaubi.lavakord.rest.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import json.*
import json.src.NANO_IP_ROUTE_PLANNER
import json.src.ROTATING_IP_ROUTE_PLANNER
import json.src.ROTATING_NANO_IP_ROUTE_PLANNER
import kotlinx.atomicfu.atomic
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val ADDRESS = "1.3.3.7"

class RoutePlannerTest {
    private val routePlannerClass = atomic<RoutePlannerStatus.Class?>(null)

    private val engine = RestHttpEngine {
        addHandler { request ->
            checkAuth(request) {
                when (request.url.fullPath) {
                    "/v3/routeplanner/free/all" -> respond("", HttpStatusCode.NoContent)
                    "/v3/routeplanner/free/address" -> {
                        val body = request.body.toByteArray().decodeToString()
                        val json = json.parseToJsonElement(body) as JsonObject
                        if ((json["address"] as? JsonPrimitive).toString().replace("\"", "") == ADDRESS) {
                            respond("", HttpStatusCode.NoContent)
                        } else {
                            respondError(HttpStatusCode.BadRequest)
                        }
                    }

                    "/v3/routeplanner/status" -> {
                        when (routePlannerClass.value) {
                            null -> respondJson("{}")
                            RoutePlannerStatus.Class.RotatingIpRoutePlanner -> respondJson(ROTATING_IP_ROUTE_PLANNER)
                            RoutePlannerStatus.Class.NanoIpRoutePlanner -> respondJson(NANO_IP_ROUTE_PLANNER)
                            RoutePlannerStatus.Class.RotatingNanoIpRoutePlanner -> respondJson(
                                ROTATING_NANO_IP_ROUTE_PLANNER
                            )
                        }
                    }

                    else -> respondError(HttpStatusCode.NotFound)
                }
            }
        }
    }

    private val lavakord = RestTestLavakord(engine)
    private val node = TestNode(lavakord)

    @JsName("unmarkAllAddresses")
    @Test
    fun `unmark all addresses`() {
        runBlocking {
            node.unmarkAllAddresses()
        }
    }

    @JsName("unmarkAddress")
    @Test
    fun `unmark address`() {
        runBlocking {
            node.unmarkAddress(ADDRESS)
        }
    }

    @JsName("noAddressStatus")
    @Test
    fun `test error handling if no route planner is configured`() {
        routePlannerClass.value = null
        runBlocking {
            assertNull(node.addressStatusOrNull())
            assertFailsWith<NoRoutePlannerException> { node.addressStatus() }
        }
    }

    @JsName("testRotatingNanoIpRoutePlanner")
    @Test
    fun `test address status on RotatingNanoIpRoutePlanner`() {
        routePlannerClass.value = RoutePlannerStatus.Class.RotatingNanoIpRoutePlanner
        runBlocking {
            node.addressStatus().run {
                assertTrue(this is RotatingNanoIpRoutePlanner)
                `class` shouldBe RoutePlannerStatus.Class.RotatingNanoIpRoutePlanner
                details {
                    ipBlock.validate()
                    failingAddresses.validate()
                    blockIndex shouldBe 0
                    currentAddressIndex shouldBe 36792023813
                }
            }
        }
    }

    @JsName("testRotatingIpRoutePlanner")
    @Test
    fun `test address status on RotatingIpRoutePlanner`() {
        routePlannerClass.value = RoutePlannerStatus.Class.RotatingIpRoutePlanner
        runBlocking {
            node.addressStatus().run {
                assertTrue(this is RotatingIpRoutePlanner)
                `class` shouldBe RoutePlannerStatus.Class.RotatingIpRoutePlanner
                details {
                    ipBlock.validate()
                    failingAddresses.validate()
                    rotateIndex shouldBe "1"
                    ipIndex shouldBe "1"
                    currentAddress shouldBe "1"
                }
            }
        }
    }

    @JsName("testNanoIpRoutePlanner")
    @Test
    fun `test address status on NanoIpRoutePlanner`() {
        routePlannerClass.value = RoutePlannerStatus.Class.NanoIpRoutePlanner
        runBlocking {
            node.addressStatus().run {
                assertTrue(this is NanoIpRoutePlanner)
                `class` shouldBe RoutePlannerStatus.Class.NanoIpRoutePlanner
                details {
                    ipBlock.validate()
                    failingAddresses.validate()
                    currentAddressIndex shouldBe 1
                }
            }
        }
    }
}
