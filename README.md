Exam sem 3 fall 2025

Here you can get an overview over how much of the api that is working:

| US | Status                                                                                                           |
|----|------------------------------------------------------------------------------------------------------------------|
| 1  | ✅                                                                                                                |
| 2  | ✅                                                                                                                |
| 3  | ✅                                                                                                                |
| 4  | ✅                                                                                                                |
| 5  | ✅ "If the candidate has no skills, enrichment returns an empty list" (Dont know if it work that way right now:)) |  
| 6  | ❌                                                                                                                |
| 7  | (✅) Test made for CandidateDAO (JUNIT) and CandidateController for REST - maybe some of them is missing          |
| 8  | ❌ NOTE: SecurityLayer added but not in use - ready to add                                                        |

Note for us-8: didn't want to crash all the tests by add roles for endpoints

| **Route**                                                | **Functionality**                                                   |
| -------------------------------------------------------- | ------------------------------------------------------------------- |
| **`/api/populate`**                                      | Populate database with initial sample data                          |
| **`GET /api/candidates`**                                | Returns all candidates including skill stats                        |
| **`GET /api/candidates/{id}`**                           | Returns candidate details including skills                          |
| **`POST /api/candidates`**                               | Creates a new candidate                                             |
| **`PUT /api/candidates/{id}`**                           | Updates an existing candidate                                       |
| **`DELETE /api/candidates/{id}`**                        | Deletes a candidate                                                 |
| **`PUT /api/candidates/{candidateId}/skills/{skillId}`** | Links an existing skill to a candidate                              |
| **`GET /api/candidates?category={category}`**            | Filters candidates based on their skills’ category (JPA or streams) |
| **`GET /api/reports/candidates/top-by-popularity❌ `**      | Returns the candidate with the highest average popularity score     |


