# A Toolbox for Personal Use

> Would like to integrate some of my works into a collection of web services.

contains:

1. A web parsing service for displaying [Signalbash](https://signalbash.com/) activities on a [Rainmeter](https://www.rainmeter.net/) skin.
2. An [example](https://spring.io/guides/tutorials/rest) of payroll service for practice.

## Requirements

- [Java runtime environment](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) >= 17
- [maven](https://maven.apache.org/)

## Usage

```bash
cd toolbox
mvn spring-bool:run
```

For the build version, see [this](https://github.com/XiaWuSharve/toolbox/releases/tag/Release) and execute the following command:
```bash
java -jar /path/to/demo-0.0.1-SNAPSHOT.jar
```

## Guides for Services, Respectively

### 1. Signalbash Parser Service
Can parse a user's name, biography, current project, today's DAW usage time, and progress time.
#### Usage: Username *xiawusharve* for Example

##### Get Profile

```bash
# On linux
curl -v localhost:8080/signalbash/xiawusharve
```
return response body:
```json
{
  "user_display_name": "XiawuSharve",
  "bio": "Urban Stray Cat.",
  "currently_making": "Music Theory",
  "_links": {
    "self": {
      "href": "http://localhost:8080/signalbash/xiawusharve"
    },
    "activity": {
      "href": "http://localhost:8080/signalbash/xiawusharve/activity"
    }
  }
}
```

##### Get Activities

```bash
# On linux
curl -v localhost:8080/signalbash/xiawusharve/activity
```
return response body:
```json
{
  "dawTime": "0 minutes",
  "streak": 0,
  "_links": {
    "self": {
      "href": "http://localhost:8080/signalbash/xiawusharve/activity"
    },
    "profile": {
      "href": "http://localhost:8080/signalbash/xiawusharve"
    }
  }
}
```

#### Rainmeter skin Config File Example

```ini
[Rainmeter]
Update=1000
AccurateText=1
BackgroundMode=2
SolidColor=0,0,0,100
DynamicWindowSize=1

[Metadata]
Name=DawUsageDisplayer
Author=Commie
Information=Displays DAW usages and steaks as well as the profile of one user.
License=Creative Commons BY-NC-SA 3.0
Version=1.0.0

[Variables]
Username=xiawusharve

[measureActivity]
Measure=WebParser
UpdateRate=30
URL=http://localhost:8080/signalbash/#Username#/activity
RegExp=(?si)"dawTime":"((?:\\"|.)*?)".*?"streak":(\d+)

[measureProfile]
Measure=WebParser
UpdateRate=30
URL=http://localhost:8080/signalbash/#Username#
RegExp=(?si)"user_display_name":"((?:\\"|.)*?)".*?"bio":"((?:\\"|.)*?)".*?"currently_making":"((?:\\"|.)*?)"


[measureName]
Measure=WebParser
URL=[measureProfile]
StringIndex=1

[measureBio]
Measure=WebParser
URL=[measureProfile]
StringIndex=2

[measureCurrentlyMaking]
Measure=WebParser
URL=[measureProfile]
StringIndex=3

[measureDawTime]
Measure=WebParser
URL=[measureActivity]
StringIndex=1

[measureStreak]
Measure=WebParser
URL=[measureActivity]
StringIndex=2

[universalStyle]
FontColor=#FFFFFF
FontSize=12
FontFace=Arial
AntiAlias=1

[meterTitle]
Meter=String
Padding=0,10,0,10
DynamicVariables=1
X=(#CURRENTCONFIGWIDTH#/2)
MeasureName=measureCurrentlyMaking
MeterStyle=universalStyle
StringAlign=Center
Text=正在进行：%1

[meterStreak]
Meter=String
Padding=10,0,10,10
MeasureName=measureStreak
Text=已进行 %1 Day(s)
DynamicVariables=1
X=(#CURRENTCONFIGWIDTH#/4)
Y=0R
MeterStyle=universalStyle
StringAlign=Center

[meterDawActivity]
Meter=String
Padding=10,0,10,10
MeasureName=measureDawTime
DynamicVariables=1
X=(#CURRENTCONFIGWIDTH#/4*3)
Text=今日已使用 %1
Y=0r
MeterStyle=universalStyle
StringAlign=Center

[meterProfile]
Meter=String
MeasureName=measureName
MeasureName2=measureBio
Text=%1: %2
DynamicVariables=1
X=0
Y=0R
Padding=10,0,10,10
W=300
MeterStyle=universalStyle
FontSize=9
ClipString=2
```
