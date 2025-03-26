Feature: Create Song
  Scenario: Create a song with valid data
    Given the user provides a valid song creation request with:
      | name       | Song1   |
      | artist     | Artist1 |
      | album      | Album1  |
      | year       | 2019    |
      | length     | 3:00    |
      | resourceId | 1       |

    When the user submits the registration request to the API endpoint "/songs"
    Then the response status should be 200
    And the response body should contain:
      | id | 1 |