/*
 * Copyright 2013 Jari Zwarts
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pro.jariz.reisplanner.misc;

import de.keyboardsurfer.android.widget.crouton.Style;
import pro.jariz.reisplanner.R;

/**
 * Created by JariZ on 19-5-13.
 */
public class CroutonStyles {
    public static Style infoStyle = new Style.Builder()
            .setBackgroundColor(R.color.crouton_info)
            .setTextColor(android.R.color.white)
            .build();

    public static Style successStyle = new Style.Builder()
            .setBackgroundColor(R.color.crouton_success)
            .setTextColor(android.R.color.white)
            .build();

    public static Style errorStyle = new Style.Builder()
            .setBackgroundColor(R.color.crouton_error)
            .setTextColor(android.R.color.white)
            .build();
}
