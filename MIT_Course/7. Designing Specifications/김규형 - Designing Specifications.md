# ****Designing Specifications (23.09.24)****

### 이 장의 목표

- underdetermined specs(결정되지 않은 스펙)을 이해하고 식별하고 평가할 수 있다.
- 선언과 옵셔널을 이해하고 선언 사양을 작성할 수 있다.
- 전제조건과 사후 조건의 스펙을 이해하고 스펙 강도와 비교할 수 있다.
- 적절한 스펙을 논리적으로 쓸 수 있습니다.

## Introduction

- 유사한 기능에 대한 스펙을 비교하고 그것들에 상충 관계에 대해 이야기한다.
    - **얼마나 결정적인가?** : 스펙은 주어진 입력에 대한 가능한 출력만 정의하는가? 아니면 가능한 아웃풋의 집합 중에 하나를 선택하는가?
    - **얼마나 선언적인가?** : 스펙은 출력이 무엇인지를 말하는가? 아니면 출력이 어떻게 이뤄지는지를 말하는가?
    - **얼마나 강한가?** : 스펙은 작거나 큰 가능한 구현 집합이 있는가?

## ****Deterministic vs. underdetermined specs****

```
static int findFirst(int[] arr, int val) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == val) return i;
    }
    return arr.length;
}

```

```
static int findLast(int[] arr, int val) {
    for (int i = arr.length - 1 ; i >= 0; i--) {
        if (arr[i] == val) return i;
    }
    return -1;
}
```

- first와 last는 임시적인 명칭
- `find` 의 명세
    
    ```
    static int findExactlyOne(int[] arr, int val)
    requires: val occursexactly once in arr
    effects:  returns index i such that arr[i] = val
    ```
    
- 이 명세는 결정론 적이다.
- 전제 조건이 만족되어질때 출력이 결정되어진다.
- 위 명세는 오직 하나의 return 값과 하나의 final static 값만 가능하다.
- 둘 이상의 유효한 출력에 대한 입력 값은 없다.
- `find` 의 또 다른 명세
    
    ```
    static int findOneOrMore,AnyIndex(int[] arr, int val)
      requires: val occurs in arr
      effects:  returns index i such that arr[i] = val
    ```
    
- 이 명세는 결정적이지 않는데 어떤 인덱스가 하나이상 반환 되어야 하는지는 적어져 있지 않다.
- 이것은 단순히 return 된 인덱스가 val임만을 의미한다.
- 이 명세는 하나의 입력에 대해 여러 출력을 허용한다.
- 비결정적 코드는 동일한 입력에 대해서도 다른 방향으로 동작할 수 있다.
    - 임의의 숫자 및, 프로레스 순서 실행에 따라서…
- 그러나 명세가 비결정적이라도 구현까지 결정적이지 않을 필요는 없다.
- 결정적이지 않은 명세는 과소 결정적(underdetermined)이라고 표현한다.
- **underdetermined** `find` 스펙은 결정적 비결정적 모두를 만족한다. 각각 **underdetermined** 방식으로 해결
- 위의 명세는 val 이 두번 이상 나타난다는 명세에 대해 어떤 인덱스가 반환되어야 하는지에 대해서는 의존하지 않는다.
- 위 명세는 또한 배열의 낮은 곳에서부터 탐색할지 높은곳에서 탐색할지에 대해 비결정적 명세 또한 만족한다.
- 결국 구현자가 이걸 선택한다.
- 미결정적 사양은 결정적 구현으로 마무리 짓는다.

## 선언 vs 운영 명세

- 명세에는 두가지 종류가 있다.
- Operational 명세는 메소드가 어떻게 작동하는지에 대한 일련의 과정을 서술
    - 일반적으로 의사코드
- Declarative 명세는 단계에 대한 내용이 아닌 최종 결과와 초기 상태에 대한 관계를 알린다.
- 대부분은 선언 명세가 좋다.
    - 이해하기 쉬움
    - 클라이언트가 의존할 수 이는 구현 세부 정보를 노출하지 않는다. → 변경 될 때 유지보수가 불가능하는 상황)
    - Ex) 배열에서 val을 발견 할때까지 아래로 내려가고 가장 낮은 인덱스가 반환된다는 것 이건 알릴 필요가 없음
    - 이걸 활용해서 유지 관리자에게 설명하는건 옳지 않다. 그냥 메소드 본문을 설명하자
- 선언적 명세는 다양한 방법이 가능하다.
    
    ```
    static boolean startsWith(String str, String prefix)
    effects: returns true if and only if there exists String suffix
                such that prefix + suffix == str
    
    ```
    
    ```
    static boolean startsWith(String str, String prefix)
    effects: returns true if and only if there exists integer i
                such that str.substring(0, i) == prefix
    
    ```
    
    ```
    static boolean startsWith(String str, String prefix)
    effects: returns true if the first prefix.length() characters of str
                are the characters of prefix, false otherwise
    ```
    

## Stronger vs weaker specs

- 메소드를 변경하고자 할 때 해당 메소드가 의존하는 사양이 있다. 둘 다에게 안전성을 보장하고 싶은 경우 어떻게 비교할 것인가?
- S2 가 S1보다 강하거나 같으면
    - S2의 전제조건이 S1의 전제조건보다 약하거나 같고
    - S2의 전제조건을 만족하는 상태에서 S2의 후조건은 S1의 후조건보다 강하거나 같을때
- 이러한 경우 S2를 만족하는 구현을 통해 S1 또한 만족 할수 있다.
- 더 나아가 S1는 S2로 대체가 가능하다.
- 이러한 두 가지 규칙은 몇개의 아이디어가 생긴다.
- 전제 조건을 약하게 해서 사용자를 만족시키고 메소드내의 약속을 통해 사후조건을 강하게 하는 방법이 있다.
    
    ```
    static int findExactlyOne(int[] a, int val)
    requires: val occursexactly once in a
    effects:  returns index i such that a[i] = val
    ```
    
    ```
    static int findOneOrMore,AnyIndex(int[] a, int val)
    requires: val occursat least once in a
    effects:  returns index i such that a[i] = val
    ```
    
    - 전제 조건은 약해졌다.
    
    ```
    static int findOneOrMore,FirstIndex(int[] a, int val)
    requires: val occursat least once in a
    effects:  returnslowest index i such that a[i] = val
    ```
    
    - 하지만 후조건은 강해졌다!

## Digramming Specifications

- 모든 자바 메소드를 하나의 그림으로 상상한다면…
- 모든 구현 가능한 공간의 영역을 정의하고 지정된 구현은 명세와 각 사전 사후 조건 관계에 따라 영역에 집어 넣는다.
- 구현의 경계선을 통해 메소드의 구현 범위 또한 알 수 있다.
- Client는 이 다이어그램을 보고 명세 내부를 자유롭게 확인하고 변경이 가능하다.
- 또한 Client의 변경에도 유연성을 제공한다.
- 유사한 명세의 경우 어떻게 다른것과 관련을 지을 수 있는가?
    - 우리는 S1로 시작하여 S2를 생성한다고 가정한다
    - 전제 조건을 강화한다.
        - s2 > s1 이면 s2가 강한 사양으로 인지
        - 사후 조건을 강화하는것은 구현자에게 어떤 의미인것인가? → 실행자의 자유는 적어지고 output의 요구 사항은 더 강해진다.
        - 가장 낮은 index i 를 구현하도록 사후 조건을 강화한다면…
        - `find OneOrMore,AnyIndex` 를 내부 구현을 수정한다. → `find OneOrMore,FirstIndex`
        - `find OneOrMore,AnyIndex` 를 보이면서  `find OneOrMore,FirstIndex`   를 내부에 구현 할 수 있을까? 사후조건이 더강력한 `find OneOrMore,FirstIndex` 을 사용한다.
- 다이어그램에서 강한 명세일수록 더 작은 영역을, 약한 명세이다면 더 큰 영역을 정의함을 볼 수 있다.
- `find Last` 는 모든 다이어그램에서 만족하지만 그곳에서는 `find OneOrMore,FirstIndex` 는 만족하지 못할 것이다…

## Designing good Specifications

- 우수한 메소드란 무엇인가? 주로 명세를 작성한다는것
- 명세는 읽기 쉬어야하고 깔끔하게 잘 작성해야한다.
- 명세서의 내용을 규정하기는 어렵다 하지만 몇가지 지침이 있다.

### ****The specification should be coherent****

- 명세에는 다양한 case를 사용하지 말아야한다 깊게 중첩된 분기문이 주로 예시
    
    ```
    static int sumFind(int[] a, int[] b, int val)
    effects: returns the sum of all indices in arrays a and b at which
                 val appears
    ```
    
- 이건 잘 설계 된 명세인가?
    - 아마 그렇진 않을것이다.
    - 관련 없는 두 작업 (두 array에서 찾고 합산)을 수행하기에 일관성이 떨어진다.
    - 이런 경우 두개의 개별 절차를 적자
- 또 다른 예시
    
    ```java
    public static int LONG_WORD_LENGTH = 5;
    public static String longestWord;
    
    /**
     * Update longestWord to be the longest element of words, and print
     * the number of elements with length > LONG_WORD_LENGTH to the console.
     * @param words list to search for long words
     */
    public static void countLongWords(List<String> words)
    ```
    
    - 전역 변수를 사용하는 것도 일관성이 없다.
    - 이것 또한 두 가지 절차로 분리 한다면 조금 더 우수한 명세

### ****The results of a call should be informative****

- 또 다른 하나의 명세
    
    ```
    static V put (Map<K,V> map, K key, V val)
    requires: val may be null, and map may contain null values
    effects:  inserts (key, val) into the mapping,
                  overriding any existing mapping for key, and
                  returns old value for key, unless none,
                  in which case it returns null
    ```
    
- 전제 조건이 null이 저장 될 수 있도록 적으세요
- 사후 조건에는 null 이 값을 찾지 못했다는 특별한 값으로 사용된다.
- 이것은 null이 리턴 될 때 실제로 바인딩이 된건지 알 수 없다.
- 삽입해서 null 인지 아니면 그냥 null인지 알 수 없는 이 디자인은 좋지 않다.

### ****The specification should be strong enough****

- 사양은 일반적인 경우에서도 충분히 강력한 보증을 가져야한다.
- 특별한 케이스의 경우 메소드 사용에 있어 특별한 주의를 가져야한다.
- 예를 들어 허용되지 않은 값이나 인자에 대해 예외를 던지지 않을 경우
- 사용자가 실제로 어떤게 변화되었는지 결정하지 못할 때 애매한 값에 대해 exception을 던지는 것에 대해서는 별 의미가 없다.
- 여기 그 예시가 있다.
    
    ```
    static void addAll(List<T> list1, List<T> list2)
    effects: adds the elements of list2 to list1,
                 unless it encounters a null element,
                 at which point it throws a NullPointerException
    ```
    
- 만약 `nullPointException` 이 던져 졌을때, 고객들은 lis1인지 list2인지 애매해진다.

### ****The specification should also be weak enough****

- 파일을 여는 방식에 따른 메소드
    
    ```
    static File open(String filename)
    effects: opens a file named filename
    ```
    
- 나쁜 명세의 대표적 예시
- 중요한 세부사항이 부족하다.
- 파일이 이미 열렸는지 아니면 생성되었는지 모른다.
- 파일을 열 방법이 없기 때문에 너무나 강력하다.
- 또한 다양한 문제가 있을 수 있다 (파일을 열 수 있는 권한이 없거나… 통제 파일 부재)
- 파일을 여는데 성공한다면 그에 대한 적절한 내용을 표시해야하는 등 더 약한 내용을 표시해야한다.

### ****The specification should use *abstract types* where possible**

- 앞선 Basic Java에서 추상적인 개념을 구별하는법을 공부 했다.
- 추상 타입에 대한 명세를 작성할 때 사용자와 구현자 모두 더 많은 자유를 가진다.
- 자바에서 종종 사용되는 `List` 나 `Map` 등은 다음과 같은 명세를 고려한다.
    
    ```
    static ArrayList<T> reverse(ArrayList<T> list)
    effects: returns a new list which is the reversal of list, i.e.
                 newList[i] == list[n-i-1]
                 for all 0 <= i < n, where n = list.size()
    ```
    
- 사용자는 어떤 `List` 를 사용할지 모르기에 `List` 로  추상화 하는것은 현명한 선택이다.

## ****Precondition or postcondition?****

- 또 다른 설계의 문제는 `Precondition` 의 여부이다.
- 전제조건을 사용시 메소드 코드에서 이에 대한 확인 여부가 필요하다.
- 실제로 전제조건을 사용하는 이유는 메소드 내에서 이를 전제 조건 없이는 힘들기 때문에 이뤄진다.
- 하지만 전제 조건은 사용자에게 불편을 겪게 한다.
- 하지만 이렇게 하지 않는다면 오류를 복구 할수 있는 예측가능한 방법이 없다.
- 그래서 유저들은 전제 조건을 좋아하지 않는다.
- 자바 API는 적절하지 않은 인자가 확인 될 때 `PostCondition` 으로 예외를 던지도록 설정한다,.
- 이러한 방식은 호출자가 버그를 쉽게 찾을 수 있다.
- `Fall Fast` 방식 달성 가능
- 또한 이러한 방법으로 이진 탐색 때 배열이 정렬되었는지(전제조건)을 미리 확인해 비용을 줄인다.
- 전제조건의 사용 유무는 공학적 판단 핵심 요소는 점검 비용과 방법의 범위를 색출 하는 것
- 로컬로만 호출 시 모든 경우를 다 체크하기 가능하다
- 하지만 모든 개발자가 사용하는 API의 경우는 자바 API처럼 해야한다.

## ****About access control****

- 일반적으로 우리는 `public` 을 사용한다.
- 이러한 방식은 프로그램 어디서든지 접근하게 한다.
- 하지만 이러한 방식은 프로그램이 로컬로만 사용되지 않는다.
- 즉 코드가 변경될 준비가 되지 않았다.
- 이런 내부 도우미 메소드가 공개 된다면 인터페이스에 혼란이 가중된다.
- `private` 를 활용하는 것은 모듈을 더 작고 일관성 있게 한다.
- 또한 이해하기 쉬워진다.
- 또한 버그로도 안전해진다.

## ****About static vs. instance methods****

- 또한 논의 없이 `static` 을 사용하는것도 문제가 된다.
- `static` 은 일반적으로 정적 객체에서 불러와진다.
- 일반적으로 지금까지 사용된 예
    
    ```
    static int find(int[] arr, int val)
    requires: val occurs in arr
    effects:  returns index i such that arr[i] = val
    ```
    
- `int []` 을 사용하여 이걸 대신한다.
    
    ```
    int find(int val)
    requires: val occurs in this array
    effects:  returns index i such thatthe value at index i in this array
                  is val
    ```
    

## 요약

- 명세는 구현자와 사용자 간의 중요한 방화벽의 역할을 한다.
- 소스코드를 전부 볼 필요 없이 이용 가능한 모듈을 개발이 가능하다.
- 구현자 또한 설계예 도움
- 선언적 명세가 가장 유용하다.
- 전제 조건은 이용자에게 부담을 주지만 결과적으로 가장 도움이 된다.
