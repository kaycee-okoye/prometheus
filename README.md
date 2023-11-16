# Prometheus - UI Design Tool

Prometheus is a Java-based desktop application designed to be a user-friendly UI design tool. With Prometheus, users can effortlessly create wireframes and export their designs as source code. The tool offers customization options, including the choice of programming language, thanks to its integration with SQLite databases.

## Features

- **User-Friendly UI Design:** Create wireframes easily with an intuitive user interface.
- **Export Customization:** Customize export parameters, including the choice of programming language, using SQLite databases.

## Project Structure

The project is organized into several folders:

- **Constants:** Contains UI/UX constants used throughout the application.
- **Controllers:** Includes controllers for file I/O and SQLite database transactions.
- **Views:** Holds the application's UI windows.
- **Models:** Contains data classes used throughout the application.
- **LanguageModels:** Stores code templates used in converting designs to source code.

## Dependencies

The project relies on SQLite for database functionality. Ensure that you have the necessary dependencies installed before running the application.

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/kaycee-okoye/Prometheus.git
   cd Prometheus
   ```

2. Run the `Prometheus.java` file:

   ```bash
   javac Prometheus.java
   java Prometheus
   ```

3. Explore the features and functionalities of the Prometheus application.

## Contribution Guidelines

Please read the [CONTRIBUTING.md](CONTRIBUTING.md) file for details on how to contribute to this project.

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE) file for details.

---

Feel free to contribute and make Prometheus even better! If you encounter any issues or have suggestions, please open an issue in the [Issue Tracker](https://github.com/kaycee-okoye/Prometheus/issues). We appreciate your contributions!

Happy coding!
