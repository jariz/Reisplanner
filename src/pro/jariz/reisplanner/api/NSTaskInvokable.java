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

package pro.jariz.reisplanner.api;

import android.util.Log;
import com.actionbarsherlock.app.SherlockFragment;
import com.devspark.progressfragment.ProgressSherlockFragment;

public abstract class NSTaskInvokable extends SherlockFragment {
	public void Invoke(Object Result, Integer TaskType) {
		Log.i("NSAPI", "Tried to invoke a fragment that hasn't overidden Invoke");
	}

    public void onSlideMenu() {

    }

}
