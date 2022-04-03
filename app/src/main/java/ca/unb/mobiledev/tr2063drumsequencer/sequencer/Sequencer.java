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

package ca.unb.mobiledev.tr2063drumsequencer.sequencer;

import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;

/**
 * @class Sequencer This class provides the functionality for delivering
 *        different sounds at different points in the time. This is done using a
 *        sequencer matrix like the one below:
 * 
 *        <pre>
 *  -----------------------------------
 * |   |   |   |   |   |   |   |   |   |
 *  -----------------------------------
 * |   |   |   |   |   |   |   |   |   |
 *  -----------------------------------
 *   1   2   3   4   5   6   7   8   9
 * </pre>
 * 
 *        This structure is contained in the class Matrix.
 * @see Matrix
 * @author claudio
 */
public class Sequencer {
    // attributes
    private int rows; // no. of samples

    private int beats; // no. of time divisions

    private int[] samples; // array of samples

    private int bpm;

    private SoundPool sound;

    private Context context;

    private Runnable playback;

    private boolean playing = false;

    private OnBPMListener mOnBPMListener;

    private Matrix matrix;

    private AudioAttributes attributes;
    private AsyncPlayer[] players;

    private Uri[] uris;

    public interface OnBPMListener {
        /**
         * This method is called every time there's a new beat.
         * 
         * @param beatCount the immediately next beat position to play.
         */
        public void onBPM(int beatCount);
    }

    // constructors
    /**
     * Default constructor.
     */
    public Sequencer(Context ctx) {
        this(ctx, 4, 8);
    }

    /**
     * Concrete constructor.
     * 
     * @param nsamples Number of samples (rows).
     * @param nbeats Number of time divisions (columns).
     */
    public Sequencer(Context ctx, int nsamples, int nbeats) {
        context = ctx;
        rows = nsamples;
        beats = nbeats;
        bpm = 120;
        samples = new int[nsamples];
        //sound = new SoundPool(nsamples, AudioManager.STREAM_MUSIC, 0);
        players = new AsyncPlayer[rows];
        uris = new Uri[rows];

        attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        matrix = new Matrix(ctx, rows, beats);
    }

    // API
    /**
     * Load a sample sound from a raw resource.
     * 
     * @param sampleSrc Identifier of the raw resource.
     */
    public void setSample(int id, int sampleSrc) {
        //samples[id] = sound.load(context, sampleSrc, 1);
        players[id] = new AsyncPlayer(String.valueOf(id));
        uris[id] = Uri.parse("android.resource://" +
                context.getPackageName() +
                "/"
                + sampleSrc);
    }

    /**
     * Load a sample sound from a file path.
     * 
     * @param path String with the path to the sound file.
     */
    public void setSample(int id, Uri path) {
        //samples[id] = sound.load(path, 1);
        players[id] = new AsyncPlayer(String.valueOf(id));
        uris[id] = path;
    }

    /**
     * Set a cell to enabled.
     * 
     * @param sampleId The row of the matrix where the cell is.
     * @param beatId The column of the matrix where the cell is.
     */
    public void enableCell(int sampleId, int beatId) {
        matrix.setCellValue(sampleId, beatId, 1);
    }

    /**
     * Set a cell to enabled.
     * 
     * @param sampleId The row of the matrix where the cell is.
     * @param beatId The column of the matrix where the cell is.
     */
    public void disableCell(int sampleId, int beatId) {
        matrix.setCellValue(sampleId, beatId, 0);
    }

    public void setOnBPMListener(OnBPMListener l) {
        this.mOnBPMListener = l;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public void addColumns(int ncol) {
        this.beats = this.beats + ncol;
    }

    public void deleteColumns(int ncol) {
        this.beats = this.beats - ncol;
    }

    /**
     * Start the playback. This function goes through an infinite loop (until it
     * is stopped using the stop() method). The matrix of samples and beats is
     * divided by the number of beats. On each iteration, a BPM callback is sent
     * back to the objects that were registered on the OnBPMListener().
     */
    public void play() {
        // play sound periodically
        playback = new Runnable() {
            int count = 0;

            public void run() {

                while (playing) {
                    if (mOnBPMListener != null)
                        mOnBPMListener.onBPM(count);
                    long millis = System.currentTimeMillis();
                    for (int i = 0; i < rows; i++) {
                        System.out.println("Row-COl " + i + "-" + count);
                        if (matrix.getCellValue(i, count) != 0)
                            //sound.play(samples[i], 100, 100, 1, 0, 1);
                            players[i].play(context, uris[i],
                                    false,
                                    attributes);
                    }

                    count = (count + 1) % beats;
                    long next = (60 * 1000) / bpm;
                    try {
                        Thread.sleep(next - (System.currentTimeMillis() - millis));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        playing = true;
        Thread thandler = new Thread(playback);
        thandler.start();
    }

    public void clear() {
        for (int rows = 0; rows < samples.length; rows++)  {
            for (int cols = 0; cols < beats; cols++) {
                disableCell(rows, cols);
            }
        }
    }

    /**
     * Stop the playback.
     */
    public void stop() {
        playing = false;
    }

    /**
     * Toggle the reproduction
     */
    public void toggle() {
        if (playing) {
            stop();
        } else {
            play();
        }
    }

}
