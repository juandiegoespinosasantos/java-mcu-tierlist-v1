# Code Style

## General
- All generated Java classes should contain the following Javadoc header:
```
/**
 * {{BRIEF DESCRIPTION OF THE CLASS}}
 *
 * @author jdespinosa0014@outlook.com
 * @version {{DATE IN MMM DD, YYYY FORMAT. EX: Dec 31, 2026. JUST SET ONCE ON CLASS CREATION, NOT UPDATED AFTERWARDS}}
 * @since {{JAVA VERSION}}
 */
 ```
- Use typed values. Never set variables with `var`.
- Never use `import *`. Import the required classes only.

## Database
- Table names must always be singular (for example `user` instead of `users`)
- If Primary Key is set to be auto-increment value, the column name must be `id`. If set to be a text value, the column name must be `code`.
- Foreign Keys must be the same name that the table (for example if `user` table has a FK to `movie` table, column name must be `movie` instead if `movie_id`).


## Entities and DTOs
- From Lombok use the following annotations:
    - `@Data`, instead of hard-coded getters, setters, `toString()`, etc.
    - `@NoArgsConstructor`
    - `@AllArgsConstructor`
    - `@Builder`, only if the class has 4 or more attributes. 
- Implement `Serializable` class and set its correspondent `serialVersionUID`. This value should be unique (never set `1L`).
- For entities, use Object types instead of primitive types on fields that map columns (for example, `Integer` instead of `int`).
- For entities, always add `@Column` annotation on fields mapping columns. Set the field name same as column name but with the Java naming convention (for example `original_title` is `originalTitle`).