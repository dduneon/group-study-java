# Version Control

# Introduction

**소프트웨어 엔지니어링 세계에서는 `Version control system`은 필수적이다. 크든 작든 없으면 불편하고 짜증 날 것이다.**

**우리는 이미 버전관리를 해봤음**

1. Drop box
2. 실행 취소/ 다시 실행
3. 버전 버호가 있는 파일 복사본 보관

![Untitled](./Image/kys1651/Untitled.png)

---

# Inventin version control

엘리스라는 사람이 있다고 가정 할 때 예시등을 설명해줌

프로그래머가 혼자 작업하는 시나리오를 고려하면 `version control` 에서 지원해야할 것들이 있음

- 과거 버전으로 되돌아가기
- 다른 버전과의 다른 점 비교
- 다른 위치로 버전을 푸시
- 해당 위치에서 기록을 가져오기
- 동일한 이전 버전에서 파생된 버전 합치기

---

## Version control terminology(버전 관리 용어)

`**Repository` : 프로젝트 버전의 로컬 또는 원격 저장소**

`**Working copy` : 작업할 수 있는 프로젝트의 편집 가능한 로컬 사본**

`**File` : 프로젝트의 단일 파일**

`**Version` or `revision` : 특정 시점의 프로젝트 내용에 대한 기록**

`**Chagne` or `diff` : 두 버전 간의 차이점**

`**Head` : 현재 가장 최신 버전**

## Features of a version control system(버전 관리 시스템의 특징)

`**Reliable` : 필요한 만큼 버전을 유지함, 백업을 허용**

`**Multiple files` : 단일 파일이 아닌 프로젝트 버전 추적**

`**Meaningful versions`: 변경 사항은 무엇이고, 변경된 위치는 무어싱ㄴ가**

`**Revert`: 이전 버전을 전체 또는 부분적으로 복원하기**

`**Compare versions`: 버전 비교**

`**Review history`: 전체 프로젝트 또는 개별 파일**

`**Not just for code`: 이미지, 글등**

`**Merge`: 공통된 이전 버전에서 분기된 버전을 결합한다.**

`**Track responsibility`: 누가 변경을 했고 누가 코드를 만졌는가?**

`**Work in parallel`: 한 명의 프로그래머가 잠시동안  스스로 작업 할 수 있도록한다.**

`**Work-in-progress`: 여러 프로그래머가 완료되지 않는 작업을 공유 할 수 있다.**

---

# git

**→ [gitgnore.io](http://gitignore.io)에서 .gitignore 파일을 만들어줌**

**저장소 복제** 

**→ 깃플로우 설정(10 - 13:00~)**

**→ Pull Request 기능을 통해서 코드 리뷰를 함**

**base: merge 받는 branch** 

**compare : merge 주는 branch**

**→ 웹 훅 기능을 위해서 업무 번호를 넣어줘야한다.(두레이에서 복사 후 # 넣어준 뒤 삽입) → 댓글로 봇이 달아주어야 정상적인 것**

**remote branch를 지웠으면 Local branch또한 지워줘야한다.**

**→ Local에서 작업전에 무조건 Fetch를 통해서 만들어줘야함**

**→ Feature를 통해서 만들어준다 (업무 번호로 만들어줌)** 

**→ Pull request(base,compare 잘 확인하기)**

**→ 팀원들간에 규칙을 통해서 몇개의 승인을 할지 몇개의 규칙을 세울지 정하고 하기 Merge는 각자 하기** 

**→ 같은 파일을 동시에 커밋하려고하면 충돌 일어날 확률 +1**

---

## **충돌 발생시**

- **변경 사항에 의해서 충돌 발생(충돌 일어난 사람이 Fetch)**
- **Feature branch로 Merge해주면서 수정해주어야 함**
- **develop를 현재 conflict branch로 Merge → 오류 발생시 수정하라는 알람이 나옴**

**파일 열어보면** 

- **<<< (HEAD) : 자신이 고친 변경사항**
- **>>>> ( DEVELOP) : 올라온 변경 사항**
- **수정을 잘 해주어야 함**

---

- **realease 브랜치에 따로 두어 배포를 함(서버 기준)**
- **Release Branch를 만든 후 버전 적기**
- **Release Branch에서 배포 후 테스트등을 함**
- **종료시  왼쪽 클릭 후 Finish Git Flow Release(Main 옆 태그로 기준을 잡는다.)**
- **그 후 Push**

---

## **Git Flow Model**

![Untitled](./Image/kys1651/Untitled%201.png)

**Master : 언제나 실행 가능한 상태를 유지해야함 → 제품으로 출시될 수 있는 브랜치**

**Develop : 다음 출시 버전(을 개발하는 브랜치**

**Feature : 기능을 개발하는 브랜치**

**Release : 이번 출시 버전을 준비하는 브랜치**

**Hotfix : 출시 버전에서 발생한 버그를 수정하는 브랜치**

---

- **Fetch와 Pull**

**→ 둘 다 원격 저장소(origin)의 내용을 가져오는데 사용됨**

**Fetch는 가져온 변경 내용을 로컬에 영향을 미치지 않으며, 병합하기전에 확인하는 용도로 사용한다**

**Pull은 가져온 변경 내용을 로컬에 병합한다. 로컬에서 작업하다가 변경된 내용은 Pull하면 충돌이 일어날 수 있음**

 ****

**즉, Fetch는 원격저장소(origin)의 변경된 커밋만 가져온다.**

**Pull은 원격 저장소의 정보를 모두 가져와서 working directory까지 카피한다.**

**Pull = Fetch + Merge**