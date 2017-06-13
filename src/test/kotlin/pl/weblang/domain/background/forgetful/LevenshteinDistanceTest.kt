package pl.weblang.domain.background.forgetful

import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class LevenshteinDistanceTest : Spek({
                                         given("a Levenshtein distance determiner") {
                                             on("segment translation that contains glossary entry in unchanged version") {
                                                 val levenshteinDistance = LevenshteinDistance(
                                                         "This PV cell operating point control algorithm is inefficient",
                                                         "PV cell operating point control algorithm")
                                                 val floorMultiplier: Double = 0.9
                                                 val ceilingMultiplier: Double = 1.1

                                                 val fallsIntoRange = levenshteinDistance.fallsIntoRange(floorMultiplier,
                                                                                                         ceilingMultiplier)

                                                 it("should determine distance to fall into range") {
                                                     fallsIntoRange `should equal` true
                                                 }
                                                 it("should determine distance to be equal to perfect estimate") {
                                                     levenshteinDistance.distance `should equal` levenshteinDistance.perfectEstimate
                                                 }
                                             }

                                             on("segment translation that contains glossary entry with 2 different letters") {
                                                 val levenshteinDistance = LevenshteinDistance(
                                                         "These PV cells operating point control algorithms are inefficient",
                                                         "PV cell operating point control algorithm")
                                                 val floorMultiplier: Double = 0.9
                                                 val ceilingMultiplier: Double = 1.1

                                                 val fallsIntoRange = levenshteinDistance.fallsIntoRange(floorMultiplier,
                                                                                                         ceilingMultiplier)

                                                 it("should determine distance to fall into range") {
                                                     fallsIntoRange `should equal` true
                                                 }
                                             }

                                             on("segment translation that contains glossary entry with one word difference") {
                                                 val levenshteinDistance = LevenshteinDistance(
                                                         "These PV cell operating spot control algorithms are inefficient",
                                                         "PV cell operating point control algorithm")

                                                 it("should determine distance to be out of range with regular floor and ceiling multipliers") {
                                                     val floorMultiplier: Double = 0.9
                                                     val ceilingMultiplier: Double = 1.1

                                                     val fallsIntoRange = levenshteinDistance.fallsIntoRange(
                                                             floorMultiplier,
                                                             ceilingMultiplier)
                                                     fallsIntoRange `should equal` false
                                                 }

                                                 it("should determine distance to fall into range with more permissive multipliers") {
                                                     val floorMultiplier: Double = 0.8
                                                     val ceilingMultiplier: Double = 1.2

                                                     val fallsIntoRange = levenshteinDistance.fallsIntoRange(
                                                             floorMultiplier,
                                                             ceilingMultiplier)
                                                     fallsIntoRange `should equal` true
                                                 }
                                             }

                                         }
                                     })
