# Technical Documentation

## Purpose

This application reads social media post data from a SQL Server database, visualises total likes per platform as a bar chart, and allows exporting the chart as a PDF and the raw records as a JSON file.

## Technologies and Libraries

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Programming language |
| Swing | JDK built-in | Desktop UI framework |
| Hibernate ORM | 6.6.34.Final | Database access / ORM |
| Lombok | 1.18.44 | Boilerplate reduction |
| JFreeChart | 1.5.4 | Chart rendering |
| Jackson | 2.17.2 | JSON serialisation |
| Apache PDFBox | 3.0.3 | PDF generation |
| SQL Server JDBC | 12.2.1 | Database driver |

## Project Structure

```
src/main/java/org/example/
├── Main.java                          – Application entry point
├── HibernateUtil.java                 – Singleton SessionFactory
├── entity/
│   └── SocialMediaPost.java           – JPA entity mapped to SocialMediaPosts table
├── repository/
│   └── SocialMediaPostRepository.java – Loads all records via Hibernate HQL
├── export/
│   ├── JsonExporter.java              – Jackson-based JSON export
│   └── PdfExporter.java               – PDFBox-based PDF export
└── ui/
    └── MainFrame.java                 – Swing window, chart, export buttons

src/main/resources/
└── hibernate.cfg.xml                  – DB connection configuration
```

## Database Mapping

The `SocialMediaPost` entity maps to the `SocialMediaPosts` table:

| Java field | DB column | Type |
|---|---|---|
| `postId` | `PostID` | Integer / int |
| `username` | `Username` | String / varchar |
| `platform` | `Platform` | String / varchar |
| `postDate` | `PostDate` | LocalDate / date |
| `contentType` | `ContentType` | String / varchar |
| `likes` | `Likes` | Integer / int |
| `comments` | `Comments` | Integer / int |
| `shares` | `Shares` | Integer / int |
| `followers` | `Followers` | Integer / int |
| `hashtag` | `Hashtag` | String / varchar |

Lombok `@Getter`, `@Setter`, and `@NoArgsConstructor` eliminate boilerplate. Hibernate uses the generated accessors to hydrate entity instances.

## Chart

`MainFrame.buildChart()` aggregates the posts with Java streams:

```java
Collectors.groupingBy(SocialMediaPost::getPlatform,
    Collectors.summingInt(SocialMediaPost::getLikes))
```

The resulting map is turned into a `DefaultCategoryDataset` (sorted by total likes descending) and rendered as a JFreeChart bar chart titled **"Total Likes by Platform"**. The chart is embedded via JFreeChart's `ChartPanel` Swing component.

## PDF Export

`PdfExporter.export(JFreeChart, String)`:
1. Renders the chart to a `BufferedImage` (800 × 600 px) via `chart.createBufferedImage()`.
2. Creates an in-memory PDFBox `PDDocument` with one `PDPage` of matching dimensions.
3. Converts the image to a `PDImageXObject` with `LosslessFactory.createFromImage()`.
4. Draws it onto the page via `PDPageContentStream.drawImage()`.
5. Saves the document to `output/chart.pdf`.

## JSON Export

`JsonExporter.export(List<?>, String)`:
1. Creates a Jackson `ObjectMapper` with `JavaTimeModule` so `LocalDate` fields are written as ISO-8601 strings.
2. Writes all `SocialMediaPost` records as a pretty-printed JSON array to `output/data.json`.

## Running the Application

### Prerequisites
- Java 21 JDK
- Maven 3.x

### Configuration
The database credentials are already set in `src/main/resources/hibernate.cfg.xml`.

### Build and Run

```bash
mvn compile exec:java
```

Or run `org.example.Main` directly from IntelliJ IDEA.

### Output Files

Both export files are written to the `output/` directory in the project root (created automatically on first export):

| Button | Output file |
|---|---|
| Export as PDF | `output/chart.pdf` |
| Export as JSON | `output/data.json` |
