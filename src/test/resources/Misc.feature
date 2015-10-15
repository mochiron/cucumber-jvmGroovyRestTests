Feature: Misc Feature Kitchen sink
	As an unhappy and overworked Feature Team Lead
	I want to proof that my code works
	so that I can make the developers understand that cucumber is not (necessarily) the end of the World

@Positive @Get @Test
Scenario: Test GET with query parameter
	Given I access the resource url "/api/test1"
			And I provide parameter "param1" as "12345"
	When I retrieve the results
	Then the status code should be 200

@Positive @Post @Session
Scenario: User login (retrieve Token) with header atributes
	Given I post to the resource url "/api/session"
			And I provide header "username" as "user@email.com"
			And I provide header "signature" as "password123"
	When I retrieve the results
	Then the status code should be 200
	And it should have the field "token_type" containing the value "bearer"

@Positive @Post @test1/subtest1
Scenario: Post with a request body (from an external file)
	Given I post to the resource url "/api/test1/subtest1"
			And I provide a body request file from "requestSamples/searchOneway.json"
	When I retrieve the results
	Then the status code should be 200
	And it should have the field "kind" containing the value "qpxExpress#tripsSearch"

@Positive @Post @test2/subtest2
Scenario: Post as a logged user
	Given I access the resource url "/api/test2/subtest2"
			And I'm logged with the username "user@email.com" and the password "password123"
	When I retrieve the results
	Then the status code should be 200
