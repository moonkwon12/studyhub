# StudyHub Entity Structure

이 문서는 현재 `studyhub` 프로젝트의 JPA 엔티티 구조를 빠르게 이해하기 위한 요약 문서다.
기준 코드는 `src/main/java/studyhub/studyhub/domain` 아래 엔티티들이다.

## 전체 관계

```text
User
 ├─< StudyMember >─ Study
 ├─< FriendRequest >─ User
 ├─< StudyPost >─ Study
 ├─< Comment >─ StudyPost
 ├─< ChatRoom >─ User
 └─< ChatMessage >─ ChatRoom
```

조금 더 풀어서 보면:

```text
User 1 --- N StudyMember N --- 1 Study
User 1 --- N FriendRequest (requester)
User 1 --- N FriendRequest (receiver)
User 1 --- N StudyPost
Study 1 --- N StudyPost
User 1 --- N Comment
StudyPost 1 --- N Comment
User 1 --- N ChatRoom (userA)
User 1 --- N ChatRoom (userB)
ChatRoom 1 --- N ChatMessage
User 1 --- N ChatMessage
```

## 엔티티별 설명

### 1. User
파일: [User.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\user\User.java)

사용자 기본 정보 엔티티다.

주요 필드:
- `id`: PK
- `email`: 로그인 식별자, unique
- `password`: 암호화된 비밀번호 저장
- `name`: 사용자 이름
- `createdAt`: 생성 시각

역할:
- 인증/인가의 기준 사용자
- 스터디 생성자 및 멤버
- 게시글/댓글 작성자
- 친구 요청의 요청자/수신자
- 채팅방 참여자 및 메시지 발신자

### 2. Study
파일: [Study.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\study\Study.java)

스터디 모임 자체를 나타낸다.

주요 필드:
- `id`: PK
- `title`: 스터디 제목
- `description`: 설명
- `createdAt`: 생성 시각

역할:
- 스터디 게시판의 상위 개념
- 실제 멤버 소속은 `StudyMember`로 관리

### 3. StudyMember
파일: [StudyMember.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\studymember\StudyMember.java)

사용자와 스터디의 다대다 관계를 푼 조인 엔티티다.

주요 필드:
- `id`: PK
- `user`: 스터디에 가입한 사용자
- `study`: 가입 대상 스터디
- `role`: `LEADER`, 기타 멤버 역할
- `joinedAt`: 가입 시각

역할:
- 한 사용자가 여러 스터디에 속할 수 있게 함
- 한 스터디에 여러 사용자가 속할 수 있게 함
- 리더 권한 이전, 탈퇴 제한 같은 정책의 기준

비즈니스 규칙:
- 스터디 생성 시 생성자는 `LEADER`로 자동 가입
- 리더는 바로 탈퇴할 수 없음

### 4. FriendRequest
파일: [FriendRequest.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\friend\FriendRequest.java)

친구 요청과 친구 상태를 함께 표현하는 엔티티다.

주요 필드:
- `id`: PK
- `requester`: 요청 보낸 사용자
- `receiver`: 요청 받은 사용자
- `status`: `PENDING`, `ACCEPTED`, `REJECTED`
- `createdAt`: 요청 생성 시각
- `respondedAt`: 수락/거절 시각

역할:
- 친구 요청 발송/수락/거절 이력 관리
- 현재 친구 목록 조회의 기준 데이터
- 채팅 허용 여부 판단의 기준 데이터

비즈니스 규칙:
- 자기 자신에게 친구 요청 불가
- 이미 친구이거나 대기 중이면 중복 요청 불가
- `ACCEPTED` 상태일 때만 친구로 간주

### 5. StudyPost
파일: [StudyPost.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\post\StudyPost.java)

스터디 내부 게시글 엔티티다.

주요 필드:
- `id`: PK
- `study`: 게시글이 속한 스터디
- `author`: 작성자
- `title`: 제목
- `content`: 본문
- `createdAt`: 작성 시각

역할:
- 스터디별 게시판 기능 제공
- 댓글의 상위 엔티티

비즈니스 규칙:
- 스터디 멤버만 게시글 작성/조회 가능하도록 서비스 레이어에서 제어
- 삭제는 작성자 권한 검사를 통해 제어

### 6. Comment
파일: [Comment.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\comment\Comment.java)

게시글에 달리는 댓글 엔티티다.

주요 필드:
- `id`: PK
- `post`: 대상 게시글
- `author`: 작성자
- `content`: 댓글 본문
- `createdAt`: 작성 시각

역할:
- 게시글 단위 댓글 작성/수정/삭제 지원

비즈니스 규칙:
- 스터디 멤버만 댓글 조회/작성 가능
- 작성자 본인만 수정/삭제 가능

### 7. ChatRoom
파일: [ChatRoom.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\chat\ChatRoom.java)

친구 사이의 1:1 채팅방 엔티티다.

주요 필드:
- `id`: PK
- `userA`: 참여자 1
- `userB`: 참여자 2
- `createdAt`: 채팅방 생성 시각

역할:
- 친구 전용 direct message 방 식별
- 두 사용자 사이 방을 하나만 유지

비즈니스 규칙:
- 참여자 순서는 user id 기준으로 정렬 저장
- `(user_a_id, user_b_id)` 유니크 제약으로 중복 방 방지
- 친구 관계가 아닌 사용자끼리는 생성 불가

### 8. ChatMessage
파일: [ChatMessage.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\chat\ChatMessage.java)

채팅방 안의 메시지 엔티티다.

주요 필드:
- `id`: PK
- `room`: 소속 채팅방
- `sender`: 발신자
- `content`: 메시지 내용
- `createdAt`: 전송 시각

역할:
- 1:1 채팅 내 대화 기록 저장

비즈니스 규칙:
- 채팅방 참여자만 메시지 조회/작성 가능
- 현재는 단순 텍스트 메시지만 지원

## 핵심 관계 해설

### User - Study - StudyMember
`User`와 `Study`는 직접 연결되지 않고 `StudyMember`를 통해 연결된다.
즉 스터디 가입 정보는 별도 엔티티로 관리되며, 여기에 역할(`role`)도 함께 저장된다.

### User - FriendRequest
친구 관계는 별도 `Friend` 테이블 없이 `FriendRequest`의 `ACCEPTED` 상태로 표현된다.
따라서 친구 목록은 “내가 요청자이거나 수신자인 수락 완료 요청”을 조회해서 계산한다.

### Study - StudyPost - Comment
스터디 안에 게시글이 있고, 게시글 안에 댓글이 있다.
즉 댓글은 직접 스터디에 속하지 않고 `Comment -> StudyPost -> Study`로 이어진다.

### User - ChatRoom - ChatMessage
채팅은 1:1만 지원한다.
`ChatRoom`은 참여자 두 명만 가지고, 실제 대화 내용은 `ChatMessage`에 쌓인다.
채팅 가능 조건은 `FriendRequest`의 수락 상태로 검증한다.

## 현재 설계 특징

- 명시적인 조인 엔티티 사용
  - `StudyMember`로 사용자-스터디 관계를 확장 가능하게 설계
- 친구 관계를 상태 기반으로 관리
  - `FriendRequest` 하나로 요청/수락/거절/친구 여부까지 표현
- 채팅은 REST + polling 기반
  - 현재 실시간 WebSocket 구조는 아님
- 권한 검사는 주로 서비스 레이어에서 수행
  - 스터디 멤버 여부, 친구 여부, 작성자 여부 등

## 향후 확장 포인트

- `User`
  - 프로필 이미지, 자기소개, 상태 메시지
- `Study`
  - 카테고리, 모집 인원, 모집 상태
- `StudyPost`
  - 수정 시간, 조회수, 공지 여부
- `Comment`
  - 대댓글 구조
- `ChatRoom`
  - 마지막 읽음 시각, 안 읽은 메시지 수
- `ChatMessage`
  - 읽음 여부, 메시지 타입(텍스트/이미지), 삭제 상태

## 참고 파일

- [User.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\user\User.java)
- [Study.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\study\Study.java)
- [StudyMember.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\studymember\StudyMember.java)
- [FriendRequest.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\friend\FriendRequest.java)
- [StudyPost.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\post\StudyPost.java)
- [Comment.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\comment\Comment.java)
- [ChatRoom.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\chat\ChatRoom.java)
- [ChatMessage.java](C:\Users\ok634\Desktop\studyhub\src\main\java\studyhub\studyhub\domain\chat\ChatMessage.java)
