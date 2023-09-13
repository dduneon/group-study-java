# 김규형 - Static Checking

## ****Hailstone Sequence****

- n이 짝수일 때 n / 2, n이 홀수 일때 3n + 1
    
    ```jsx
    2, 1         
    3, 10, 5, 16, 8, 4, 2, 1     
    4, 2, 1
    2n, 2n-1 , ... , 4, 2, 1
    5, 16, 8, 4, 2, 1
    7, 22, 11, 34, 17, 52, 6, 13, 40, ...? (where does this stop?)
    ```
    
- 이런 코드를 하일스톤(우박) 코드… 왜? → 왔다갔다 하니깐….

## Type

- 타입 → 값의 집합
- Primitive Types (원시 타입) *(int, long, boolean…)*
- Object Type (객체 타입) *(String, BingInteger…)*
- Operations → 입력을 받고 출력을 생성하는 함수

## Static Typing & Checking

- **Compile Time Error VS Runtime Error**
    
    ### 컴파일 타임 에러
    
    - 기계어로 컴파일 하는 과정의 에러
    - 구문 오류 또느 누락 파일 참조, 컴파일 되지 못하게 막음
    
    ### 런타임 에러
    
    - 프로그램이 실행하는 동안 발생 버그 (ex: 프로그램 충돌 등...)
    
- JAVA는 정적 타입 언어 → 컴파일 타임에 변수를 검사함 (정적 검사) 이런 효과덕분에 버그에 더 안전함
- Python은 동적 타입 언어 런타임 동안 에러를 검사
    
    

**Checking의 종류**

- **Static checking :** 프로그램 Run 이전에 버그를 자동으로 찾는것
    - 구문 에러 : 잘못된 단어나, 구두점을 검사
    - 작업 디렉토리 이름
    - 잘못된 argument(인자)
    - 잘못된 argument Type
    - 잘못된 return Type
    - 이러한 방식은 변수가 어떤 타입을 가질지는 보장하지만 런타임 시간까지 그것을 보증하지는 않는다.
- **Dynamic checking :** Run 상태가 되고 버그를 자동으로 찾는것
    - 잘못된 인자 값 (ex: x/y 에서 오직 y가 0일때 오류 이건 동적 에러)
    - 반환 값을 특정할수 없는 경우..?
    - 인덱스 범위 초과
    - null 객체를 참조할경우
- **No Chechking :** 언어가 error를 찾이 않고 스스로 버그를 고치는 행위
    
    *Static checking > Dynamice Checking > No Checking 순으로 좋음.*
    

### Primitive Types

- 자바의 Integer(다른 원시 타입)은 실제 숫자가 아니다.
- `5 / 2`의 경우 분수가 아닌 2를 리턴 → 동적 에러에서 탐지 못함
- Overflow같은 경우 int나 long의 경우 값 초과시… 이상한 값 도출
- `float` 과 `doubles`는 real number와는 조금 다르다. dynamice error(0으로 나누거나 음수에 제곱근)을 시도할경우 에러가 아닌 이상한 결과(POSITIVE_INFINITY)를 내보낸다.

## Arrays and Collections

### fixed Array

- 고정된 길이 만약 int[100] 이라면 그 이상을 넘어서면 → 버그

### List

- Class
- ArrayList, LiknedList…
- 객체를 저장한 Integer 같은 (int 와는 자동 형변환)

### Methods

- `public` 프로그램 어디에서나 사용가능…
- `static` 객체를 통해 불러지지 않음 *ex) .add() / .lenght()*
- 주석은 매우 중요하다 언제나 메소드의 대해서 추상적인 부분을 정의
    
    *ex) int n 에서 n이 양수임을 알림*
    

## ****Mutating Values vs. Reassigning Variables****

### Mutable

- 일반적인 Array, List의 경우 값 안의 참조를 변경
- StringBuiler도 마찬가지

### Immutable

- 일반적인 변수의 재할당
- String 이 대표적

### Final

- 자바가 제공하는 Immutable 예약어
- 재 할당 할려고 시도시 Compile error가 발생해 Static checking이 가능해짐

## ****Documenting Assumptions****

- 일반적으로 int 로 선언된 변수는 컴파일러가 검사한다.
- final로 선언 한다면 절대 재 할당되지 않은 것을 컴파일러가 인지한다
- 하지만 음수 값이 들어가면 안된다는 것등은 컴파일러가 모른다
- Assumption을 적는 것은 자기 또는 다른 사람이 이러한 형식을 잊이 않게 하기 위해
- 프로그램에 대해 두가지 목표를 명심해 쓸것
    - 컴파일러가 구문, 타입 에러를 잡을수 있게 할것
    - 사람이 프로그램을 이해하기 쉽게 하여 추후 수정하기 쉽게 할것
    

## Hacking VS Engineering

- BAD
    - 테스트 하기전에 너무 많은 코드를 적기
    - 모든 것을 기억할수 있다고 가정하고 주석을 적지 않은것
    - 버그가 없다고 가정하거나 쉽게 수정할 수 있다고 생각하기
- GOOD
    - 조금 작성하고 테스트, 테스트 퍼스트 프로그래밍
    - 코드가 의존하는 문서를 만들기
    - Static checking의 도움 받기

## Summary

   - **Static checking을 사용 해서 아래와 같은 목표를 도달.**

- 버그로부터 안전한 코드
    - Static Checking이 type에 대해서 runtime 이전에 도움을 줄 수있다.
- 이해하기 쉬운 코드
    - 코드에 타입이 명시적으로 적어져있기에 이해 하기 쉬움
- 변화에 준비됨
    - 코드의 변경시 영향 받는 모든 곳에 에러를 보여주면서 상기시킴