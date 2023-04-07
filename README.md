# Notes App

## Goal
This is a notes application designed for students who want a versatile app for taking quick notes, reminders, and lists, as well as taking and storing larger texts for school.

## Team members
Andy Yang - yz8yang@uwaterloo.ca <br />
Charles Shen - c2999she@uwaterloo.ca <br />
Benjamin Du - h39du@uwaterloo.ca <br />
Yuying Li - y2786li@uwaterloo.ca

## Project Review Requirements
https://docs.google.com/document/d/1t-xZfA8tPVKafwNlTAlWANI3tw-mf2jXsbvjtk_wxWA/edit

## Instructions
Here is the link to the instructions about how to install and run the application, how to run the web service, and how to test all features of the application:
https://docs.google.com/document/d/13vS-iEMVQ-AlIvGeDwtdzvYNdbUZJN4lDgqeUlMeJg4/edit?usp=sharing. You can also find a copy of this doc inside releases/application-4.0.0 and it is named "instructions".
Here is the link to the demo videos for all features and how to install and run the application: https://drive.google.com/drive/folders/1xycpsd4Em0CNGLbFwzcxzO4jjukDim28?usp=share_link

Key Points to Note:
1. Before you run the application, please make sure that the web service is running (for how to run the web service, please see the instructions doc: https://docs.google.com/document/d/13vS-iEMVQ-AlIvGeDwtdzvYNdbUZJN4lDgqeUlMeJg4/edit?usp=sharing). Otherwise, the application won't be able to run since it needs to communicate with the web service to get and update data.
2. This application saves your data and stores it to the cloud platform when you close the application window. Hence, when you close the application window, there will be around 1 second delay. 
3. You must explicitly click the "Save" button to save the content of the note. Otherwise, if you switch to another note before saving the current editing note, the content you modified won't be saved.
4. The first line in the content of a note is the title of the note, and you can not save the note with an empty or blank first line.

## Screenshots/videos
Optional, but often helpful to have a screenshot or demo-video for new users.
1. Sprint 1 Demo Video: https://drive.google.com/file/d/1LsTiXdP9FUuRdxhWlns2XkmkJr_Fmo6o/view?usp=sharing 
2. Sprint 2 Demo Video: https://drive.google.com/file/d/1knlenc1R8X-3gHzdX8KCX7pFt9WtSuRO/view?usp=share_link 
   - Group related notes: https://drive.google.com/file/d/1ZJAxs7OveGtnbKNIvc2V1TAkOOU1zsnL/view?usp=share_link
   - Add notes under a group: https://drive.google.com/file/d/1euT0NHgOt3mLLzN4b9huFprZcCC8ka_j/view?usp=sharing 
   - Search notes: https://drive.google.com/file/d/16qvf1SFVvK0bErisBdXSLV3vKZu1XHwE/view?usp=sharing
   - Sort notes: https://drive.google.com/file/d/1sp9FvwWAOXMq3xWID2MR3t0awy9EXSok/view?usp=share_link
3. Sprint 3 Demo Video: https://drive.google.com/file/d/1m9iah8dUdXidUGHBxYsgP1Tzn6jTdRSs/view?usp=sharing
4. Sprint 4 Demo Video: https://drive.google.com/file/d/161J6uVJRunUHaZLAjgqyZnUHPocx9ptt/view?usp=sharing

## Releases
Each sprint should produce a software release. Your README should include a list of each release, with a link to the release-notes. 
* see ![release page](assets/release-page.png) <br />
1. Sprint 1 Release Note: https://docs.google.com/document/d/1hh97z5v7OMlaif3IH1kHK93rcIUQVf9ReFG0zE3yVtE/edit?usp=sharing <br />
   (Sprint 1 Client Installer: https://git.uwaterloo.ca/y2786li/cs346/-/tree/main/releases/application-1.0.0/bin)
2. Sprint 2 Release Note: https://docs.google.com/document/d/1ZWt9AT4D2OfroXCth0i8RAkhIaQuSvvKBgioV4Jpz24/edit?usp=sharing <br />
   (Sprint 2 Client Installer: https://git.uwaterloo.ca/y2786li/cs346/-/tree/main/releases/application-2.0.0/bin)
3. Sprint 3 Release Note: https://docs.google.com/document/d/1qjn1o_CF1fyFPTgThamhK1ndf4a1sQxUS17kzOEaS5M/edit?usp=sharing <br />
   (Sprint 3 Client Installer: https://git.uwaterloo.ca/y2786li/cs346/-/tree/main/releases/application-3.0.0)
4. Sprint 4 Release Note: https://docs.google.com/document/d/1vBhKlXTbuiv_JgTTatkxKUYXYLQDespNuq_vuTQphTk/edit?usp=sharing <br />
   (Sprint 4 Client Installer: https://git.uwaterloo.ca/y2786li/cs346/-/tree/main/releases/application-4.0.0)


## EVERYTHING BELOW THIS IS DEFAULTED (REMOVE IF WE DONT SEE USE FOR THE TIPS)

## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://git.uwaterloo.ca/y2786li/cs346.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://git.uwaterloo.ca/y2786li/cs346/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***
