# StudyHub Simple ERD

현재 프로젝트의 엔티티 관계만 빠르게 보기 위한 축약본이다.

## ERD

```text
[users]
- id PK
- email UNIQUE
- password
- name
- created_at

[studies]
- id PK
- title
- description
- created_at

[study_member]
- id PK
- user_id FK -> users.id
- study_id FK -> studies.id
- role
- joined_at

[friend_request]
- id PK
- requester_id FK -> users.id
- receiver_id FK -> users.id
- status
- created_at
- responded_at

[study_post]
- id PK
- study_id FK -> studies.id
- author_id FK -> users.id
- title
- content
- created_at

[comments]
- id PK
- post_id FK -> study_post.id
- author_id FK -> users.id
- content
- created_at

[chat_room]
- id PK
- user_a_id FK -> users.id
- user_b_id FK -> users.id
- created_at
- UNIQUE(user_a_id, user_b_id)

[chat_message]
- id PK
- room_id FK -> chat_room.id
- sender_id FK -> users.id
- content
- created_at
```

## Relationship View

```text
users 1 --- N study_member N --- 1 studies

users 1 --- N friend_request (requester)
users 1 --- N friend_request (receiver)

studies 1 --- N study_post
users   1 --- N study_post

study_post 1 --- N comments
users      1 --- N comments

users     1 --- N chat_room (user_a)
users     1 --- N chat_room (user_b)
chat_room 1 --- N chat_message
users     1 --- N chat_message
```

## Business Meaning

- `study_member`: 사용자와 스터디의 소속 관계를 나타내는 조인 엔티티
- `friend_request`: 친구 요청 이력과 친구 상태를 함께 표현
- `chat_room`: 친구 사이의 1:1 채팅방
- `chat_message`: 채팅방 안의 개별 메시지

## Notes

- 친구 관계는 별도 `friend` 테이블 없이 `friend_request.status = ACCEPTED`로 판단한다.
- 채팅방은 `(user_a_id, user_b_id)` 유니크 제약으로 두 사용자 사이 하나만 생성된다.
- 댓글은 스터디에 직접 달리지 않고 `study_post`에 종속된다.

## Detailed Version

상세 설명은 [erd.md](C:\Users\ok634\Desktop\studyhub\docs\erd.md) 참고.
