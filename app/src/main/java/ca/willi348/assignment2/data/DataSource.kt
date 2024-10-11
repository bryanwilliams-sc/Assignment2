/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.willi348.assignment2.data

import ca.willi348.assignment2.R

object DataSource {
    val options = listOf(
        R.string.one_million,
        R.string.one_hundred_thou,
        R.string.ten_thou,
        R.string.iPhone,
        R.string.food,
        R.string.Playstation,
        R.string.five_dolla,
        R.string.gum

    )

    val quantityOptions = listOf(
        Pair(R.string.box_one, 1),
        Pair(R.string.box_two, 2),
        Pair(R.string.box_three, 3),
        Pair(R.string.box_four, 4),
        Pair(R.string.box_five, 5)
    )
}
