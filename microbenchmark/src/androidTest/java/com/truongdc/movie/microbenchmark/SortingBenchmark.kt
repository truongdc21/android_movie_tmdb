/*
 * Designed and developed by 2025 truongdc21 (Dang Chi Truong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.truongdc.movie.microbenchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.truongdc.movie.benchmarkable.SortingAlgorithms
import com.truongdc.movie.benchmarkable.isSorted
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class SortingBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val random = Random(0)

    private val unsorted = IntArray(10_000) { random.nextInt() }

    @Test
    fun benchmark_quickSort() {
        // creating the variable outside of the measureRepeated to be able to assert after done
        var listToSort = intArrayOf()

        benchmarkRule.measureRepeated {
            // copy the array with timing disabled to measure only the algorithm itself
            listToSort = runWithTimingDisabled { unsorted.copyOf() }

            // sort the array in place and measure how long it takes
            SortingAlgorithms.quickSort(listToSort)
        }

        // assert only once not to add overhead to the benchmarks
        assertTrue(listToSort.isSorted)
    }

    @Test
    fun benchmark_bubbleSort() {
        // creating the variable outside of the measureRepeated to be able to assert after done
        var listToSort = intArrayOf()

        benchmarkRule.measureRepeated {
            // copy the array with timing disabled to measure only the algorithm itself
            listToSort = runWithTimingDisabled { unsorted.copyOf() }

            // sort the array in place and measure how long it takes
            SortingAlgorithms.bubbleSort(listToSort)
        }

        // assert only once not to add overhead to the benchmarks
        assertTrue(listToSort.isSorted)
    }
}
