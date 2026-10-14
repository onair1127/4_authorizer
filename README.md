<h1 align="center">API Gateway Authorizer</h1>
<h3 align="center">앱 서버 요청 시 사용자 인증을 진행하는 모듈입니다.</h3>
<br><br>

저희 서비스는 JWT + AES256 사용자를 인증/인가합니다. API Gateway에서 기본으로 제공하는 JWT Authorizer로는 보안 로직을 해결할 수 없어 Lambda를 활용한 커스텀 인증/인가/재발급 모듈을 제작하여 사용하였습니다.

이 모듈을 추후에 사용자가 많아지게 되면 Lambda Authorizer에서 Spring Cloud Gateway로 마이그레이션하여 사용할 예정입니다.
