# Email Defender

A fast-paced email management simulation game where players must identify and respond to legitimate emails while deleting spam.

## Game Overview

In Email Defender, you take on the role of an email inbox manager trying to keep up with an ever-increasing flow of messages. Your goal is to efficiently process emails, respond to legitimate ones, and delete spam - all while managing limited inbox space.

### Core Gameplay

- **Process emails**: Respond to legitimate emails and delete spam
- **Manage inbox space**: Limited capacity that increases with upgrades
- **Handle time pressure**: Emails arrive faster as you progress
- **Upgrade your abilities**: Purchase improvements to help manage emails more efficiently

## Running the Game

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Ability to compile and run Java applications

### Compiling

1. Navigate to the directory containing the source files
2. Compile all Java files:
```
javac *.java
```

### Running

After compiling, run the game with:
```
java EmailDefender
```

## Game Controls

- **Double-click** on an email to open it
- Use the **response buttons** to handle legitimate emails
- Click **Delete** to remove spam emails
- Click **Mark as Spam** to identify spam emails
- Visit the **Upgrade Shop** to purchase improvements
- Use the **Pause/Resume** button to control game flow

## Scoring System

- +10 points for correctly responding to legitimate emails
- +5 points for correctly deleting spam
- -15 points for incorrect handling of emails
- Combo multipliers for consecutive correct actions (up to 2.0x)
- 1 coin per 10 points earned

## Upgrades

- **Inbox Capacity**: Increase maximum storage (+10 emails per level)
- **Response Speed**: Decrease time to process emails (-5% per level)
- **Spam Filter**: Auto-delete obvious spam (5% chance per level)
- **Quick Reply**: Prepare template responses (+1 template per level)
- **Search Function**: Find specific emails faster (+10% search speed)
- **Attachment Compressor**: Reduce space used by attachments (-5% space per level)

## Game Over Conditions

- Inbox reaches capacity
- Too many legitimate emails incorrectly deleted
- Critical emails missed

## File Structure

- `EmailDefender.java`: Main application class
- `Email.java`: Email class with properties and methods
- `EmailSystem.java`: Manages email generation and delivery
- `Player.java`: Manages player data like score and upgrades
- `UpgradeSystem.java`: Handles available upgrades and effects
- `GameManager.java`: Controls game flow and progression
- `MainScreen.java`: Main game interface with email list
- `EmailViewDialog.java`: For viewing and interacting with emails
- `UpgradeShopDialog.java`: For purchasing upgrades
- `UpgradeType.java`: Enumeration of upgrade types

## Tips for Success

- Prioritize upgrading your inbox capacity early
- Watch for urgent emails that require immediate attention
- Build a combo by handling emails correctly in sequence
- Balance upgrade purchases based on your play style
- Check sender names carefully - spam often comes from suspicious sources