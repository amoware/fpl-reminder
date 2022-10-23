# FPL Reminder

## Description

Android app project aiming to make sure its users never miss a Fantasy Premier League deadline

## Testing

Get list of active alarms:

```bash
adb shell dumpsys alarm
```

Make sure that alarms are persisted when:

* system time changes
* the phone powers off or restarts
