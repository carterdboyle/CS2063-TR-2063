/*******************************************************************************

   Copyright: 2011 Android Aalto Community

   This file is part of SoundFuse.

   SoundFuse is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   SoundFuse is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with SoundFuse; if not, write to the Free Software
   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 ******************************************************************************/

package ca.unb.mobiledev.tr2063drumsequencer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import ca.unb.mobiledev.tr2063drumsequencer.sequencer.Sequencer;

public class SamplerToggleListener implements OnClickListener {

    Sequencer sequencer;

    int beats;

    SharedPreferences prefs;

    public SamplerToggleListener(Sequencer sequencer, SharedPreferences prefs,
                                 Context ctx, int samples, int beats) {
        this.sequencer = sequencer;
        this.beats = beats;
        this.prefs = prefs;
    }

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();

        int samplerId = buttonId / this.beats;
        int beatId = buttonId % this.beats;
        if (!(v instanceof ToggleButton)) {
            Log.e("SampleToggleListener", "Invalid View type: " + v.getClass());
            return;
        }
        ToggleButton currentButton = (ToggleButton) v;
        if (currentButton.isChecked())
            sequencer.enableCell(samplerId, beatId);
        else
            sequencer.disableCell(samplerId, beatId);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("cell" + buttonId, currentButton.isChecked());
        editor.commit();
    }
}
