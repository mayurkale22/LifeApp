package com.mainpanel;

/**
 * Calculates and displays pace (steps / minute), handles input of desired pace,
 * notifies user if he/she has to go faster or slower.
 * 
 * Uses {@link PaceNotifier}, calculates speed as product of pace and step length.
 * 
 * @author Levente Bagi
 */
public class SpeedNotifier implements PaceNotifier.Listener, SpeakingTimer.Listener {

    public interface Listener {
        public void valueChanged(float value);
        public void passValue();
    }
    private Listener mListener;
    
    int mCounter = 0;
    float mSpeed = 0;
    
    boolean mIsMetric;
    float mStepLength;

    PedometerSettings mSettings;
    Utils mUtils;

    /** Desired speed, adjusted by the user */
    float mDesiredSpeed;
    
    /** Should we speak? */
    boolean mShouldTellFasterslower;
    boolean mShouldTellSpeed;
    
    /** When did the TTS speak last time */
    private long mSpokenAt = 0;
    
    public SpeedNotifier(Listener listener, PedometerSettings settings, Utils utils) {
        mListener = listener;
        mUtils = utils;
        mSettings = settings;
        mDesiredSpeed = mSettings.getDesiredSpeed();
        reloadSettings();
    }
    public void setSpeed(float speed) {
        mSpeed = speed;
        notifyListener();
    }
    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        mStepLength = mSettings.getStepLength();
        mShouldTellSpeed = mSettings.shouldTellSpeed();
        mShouldTellFasterslower = 
            mSettings.shouldTellFasterslower()
            && mSettings.getMaintainOption() == PedometerSettings.M_SPEED;
        notifyListener();
    }
    public void setDesiredSpeed(float desiredSpeed) {
        mDesiredSpeed = desiredSpeed;
    }
    
    private void notifyListener() {
        mListener.valueChanged(mSpeed);
    }
    
    public void paceChanged(int value) {
        if (mIsMetric) {
            mSpeed = // kilometers / hour
                value * mStepLength // centimeters / minute
                / 100000f * 60f; // centimeters/kilometer
        }
        else {
            mSpeed = // miles / hour
                value * mStepLength // inches / minute
                / 63360f * 60f; // inches/mile 
        }
        tellFasterSlower();
        notifyListener();
    }
    
    /**
     * Say slower/faster, if needed.
     */
    private void tellFasterSlower() {
        if (mShouldTellFasterslower && mUtils.isSpeakingEnabled()) {
            long now = System.currentTimeMillis();
            if (now - mSpokenAt > 3000 && !mUtils.isSpeakingNow()) {
                float little = 0.10f;
                float normal = 0.30f;
                float much = 0.50f;
                
                boolean spoken = true;
                if (mSpeed < mDesiredSpeed * (1 - much)) {
                    mUtils.say("much faster!");
                }
                else
                if (mSpeed > mDesiredSpeed * (1 + much)) {
                    mUtils.say("much slower!");
                }
                else
                if (mSpeed < mDesiredSpeed * (1 - normal)) {
                    mUtils.say("faster!");
                }
                else
                if (mSpeed > mDesiredSpeed * (1 + normal)) {
                    mUtils.say("slower!");
                }
                else
                if (mSpeed < mDesiredSpeed * (1 - little)) {
                    mUtils.say("a little faster!");
                }
                else
                if (mSpeed > mDesiredSpeed * (1 + little)) {
                    mUtils.say("a little slower!");
                }
                else {
                    spoken = false;
                }
                if (spoken) {
                    mSpokenAt = now;
                }
            }
        }
    }
    
    public void passValue() {
        // Not used
    }

    public void speak() {
        if (mSettings.shouldTellSpeed()) {
            if (mSpeed >= .01f) {
                mUtils.say(("" + (mSpeed + 0.000001f)).substring(0, 4) + (mIsMetric ? " kilometers per hour" : " miles per hour"));
            }
        }
        
    }

}

