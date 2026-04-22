# Contributing to High-Performance Spell Checker

Thank you for considering contributing to this project! Here are some guidelines to help you get started.

## 🐛 Reporting Bugs

If you find a bug, please open an issue with:
- A clear and descriptive title
- Steps to reproduce the behavior
- Expected behavior vs. actual behavior
- Your environment (JDK version, OS)

## 💡 Suggesting Features

Feature requests are welcome! Please open an issue describing:
- The problem you're trying to solve
- Your proposed solution
- Any alternatives you've considered

## 🔧 Development Setup

1. **Fork** the repository
2. **Clone** your fork:
   ```bash
   git clone https://github.com/your-username/High-Performance-Spell-Checker.git
   ```
3. **Build** the project:
   ```bash
   make build
   ```
4. **Run tests**:
   ```bash
   make benchmark
   ```

## 📝 Pull Request Process

1. Create a feature branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes following the existing code style
3. Ensure the project builds successfully with `make build`
4. Run the benchmark to verify no performance regressions
5. Update documentation if needed
6. Submit a pull request with a clear description

## 🎨 Code Style

- Use **4 spaces** for indentation (no tabs)
- Follow standard **Java naming conventions**
- Add **Javadoc** comments for all public methods
- Include **complexity annotations** where applicable

## 📜 License

By contributing, you agree that your contributions will be licensed under the MIT License.
