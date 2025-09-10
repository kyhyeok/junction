-- 코드 그룹 4개 데이터 삽입
INSERT INTO code_group (code_group, code_group_name, description) VALUES
    ('성별코드', '성별코드', '성별을 표시');

INSERT INTO code_group (code_group, code_group_name, description) VALUES
    ('방문상태코드', '방문상태코드', '환자 방문의 상태(방문중, 종료, 취소)');

INSERT INTO code_group (code_group, code_group_name, description) VALUES
    ('진료과목코드', '진료과목코드', '진료과목(내과, 안과 등)');

INSERT INTO code_group (code_group, code_group_name, description) VALUES
    ('진료유형코드', '진료유형코드', '진료의 유형(야치방, 검사 등)');

-------------------------------------------------------------------------------


-- 코드 8개 삽입
-- 성별코드 그룹의 코드들
INSERT INTO code (code_group, code, code_name) VALUES
('성별코드', 'M', '남');

INSERT INTO code (code_group, code, code_name) VALUES
    ('성별코드', 'F', '여');

-- 방문상태코드 그룹의 코드들
INSERT INTO code (code_group, code, code_name) VALUES
    ('방문상태코드', '1', '방문중');

INSERT INTO code (code_group, code, code_name) VALUES
    ('방문상태코드', '2', '종료');

INSERT INTO code (code_group, code, code_name) VALUES
    ('방문상태코드', '3', '취소');

-- 진료과목코드 그룹의 코드들
INSERT INTO code (code_group, code, code_name) VALUES
    ('진료과목코드', '01', '내과');

INSERT INTO code (code_group, code, code_name) VALUES
    ('진료과목코드', '02', '안과');

-- 진료유형코드 그룹의 코드들
INSERT INTO code (code_group, code, code_name) VALUES
    ('진료유형코드', 'D', '야치방');

INSERT INTO code (code_group, code, code_name) VALUES
    ('진료유형코드', 'T', '검사');