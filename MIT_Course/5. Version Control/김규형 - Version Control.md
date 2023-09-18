## Objectives

- version control이 무엇이고 왜 사용하는지 파악합니다.
- 어떻게 git이 version history를 그래프로 저장하는지 이해합니다.
- version history를 사용하고 읽고 만드는것을 연습합니다.

## Introduction

- Version control 시스템은 소프트웨어 공학에선 필수적인 도구입니다. 대부분의 오픈, 취미 등등에서 사용합니다.
- version control 없이 팀원의 모든 코드를 조절하는 것은 매우 어렵다.

### version control 없는 버전 제어

- 드롭박스
- undo/redo
- 파일에 번호를 붙여 저장

---

## Inventing version control(버전 컨트롤의 발명)

### 엘리스 혼자서 개발할때

- version control 없이 혼자 개발할경우 백업파일을 만든다고 가정
- Hello.java1, Hello.java2….
- 그리고 이걸 Cloud에 올린다.
- 만약 노트북에서 작업한 [HelloL.java](http://HelloL.java) 와 데스크탑에서 작업한 [HelloD.java](http://HelloD.java)
- 만약 실수로 파일을 덮어 쓴다면…? (ex: [HelloL.java](http://HelloL.java) → HelloD.java)
- 데스크탑에서 작업한 소중한 파일을 잃어버렸다…

### 앨리스와 밥이 개발할때

- 서로 다른 컴퓨터에서 작업하기에 클라우드 서버에 엄격한 규율을 적용
- 또한 효율적인 관리를 위해 로그 작성

### Muliple branches

- 새로운 기능을 추가하기 위해 브랜치에서 작업
- 기능이 완전하게 작업되기 전까지 pull 되는건 누구도 원하지 않음
- 혼자 개발하더라도 이러한것은 동일함
- 이러한 기능은 높은 유연성을 제공

## Distributed vs centralized

- 중앙집중식
    - 마스터 서버 하나에서 관리
    - 모든 사용자는 마스터서버와 작업을 공유
- 분산식
    - 개인적인 사용자가 개인 저장소를 가지고 있음
    - 개인 저장소에서 변경된다면 팀 저장소는 그에 대한 변경 내용을 결정해야함
    

### Version control 용어

- **Repository** : 로컬 또는 원격 저장소
- **Working copy** : 작업 또는 프로젝트의 편집 가능한 복사본
- **File** : 단일 파일
- **Version** : 프로젝트의 한 시점을 기록
- **diff** : 두 버전간의 차이
- **Head** : 현재 버전

### version control system의 특징

- **Realibable :** 안전하게 백업
- **Multiple files** : 프로젝트의 여러 버전을 추적
- **Meaningful Version** : 어떤 변화가 있고 어디서 만들어졌는지
- **Revert** : 이전 버전으로 복원
- **Compare version**
- **Review history** : 전체 프로젝트 또는 개인 파일에 대한 컴토
- **Not just for code** : 산문, 이미지….
- **Merge** : 이전 버전과 다른 버전을 결합
- **Track respnsibillity** : 누가 변화를 가지고, 누가 코드 라인을 만졌는지
- **Work in parallel** : 한명의 프로그래머가 다른 사람에 상관없이 혼자 버전관리 가능하다.
- **Work-In-Progress** : 다수의 프로그래머가 서로 완료되지 않은 작업을 공유 가능

## Git

- Command Line에서 작동
- 자세한 내용은 PS0에서…

### git object graph

- git에서 수행하는 모든 작업은 우리의 프로젝트 안 파일에 그래프 형식으로 저장된다.
- git clone은 이러한 그래프를 로컬로 가져오는것
- 일반적으로 directed acyclic graph(DAG) 형태
- 커밋은 일반적으로 16진수로 표시되는 고유 ID로 식별
- 각 커밋에는 부모 커밋에 대한 포인터가 있다.
- 부모가 둘인 커밋도 있을수 있음

### git commit

- 작업 디렉토리 내용을 기반으로 커밋을 생성 그래서 편집을 하면 스냅샷에 변경 사항 포함
- git은 마지막 준비 영역이 **staging area**에 저장
- **staging area**는 프로토 커밋과 같다. `git add` 를 사용하여 새로운 스냅샷을 만드는것은 다음과 같다.

> 1.  작업 디렉토리와 스테이징 영역 Head commit 모두 같다.
2. 파일을 변경
3. `git add` 를 사용해서 Stage 변경 
4. `git commit` 을 사용해서 새 커밋 만들기
> 

### Sequences, trees, and graphs

- 단일 컴퓨터에서 혼자 작업할 경우 DAG는 일반적인 시퀸스로 보임 (commit1 → commit2 → commit3…)
- 하지만 다수의 프로그래머가 동시에 변화를 만든다면? ( 여러 커밋이 하나의 부모 커밋을 가리킬때)
- 이러한 과정으로 git push, git pull이 있음

### merging

- A라는 파일을 각자 다른 두 사람이 B 와 C파일을 생성
- B가 업데이트된 A를 push
- C가 업데이트된 A를 push할 경우 push가 거절됨
- C는 B로 인해 업데이트 된 A를 pull 하고 자신의 업데이트된 A를 병합하여 새로운 A+를 생성
- C는 A+를 push하고 B또한 A+를 pull 한다.
- 이러한 방식은 서로 다른 파일을 수정했기에 가능하다…
