package toraylife.mappetextras.modules.scripting.scripts.code;

import toraylife.mappetextras.modules.scripting.scripts.user.IScriptFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptFile implements IScriptFile {
    Path path;

    public ScriptFile(Path path) {
        this.path = path;
    }


    @Override
    public Path getPath() {
        return this.path;
    }

    @Override
    public String getFullName() {
        Path fileName = this.path.getFileName();

        return fileName == null ? this.path.toString() : fileName.toString();
    }

    @Override
    public String getName() {
        int dotIndex = this.getFullName().lastIndexOf('.');

        return dotIndex <= 0 ? this.getFullName() : this.getFullName().substring(0, dotIndex);
    }

    @Override
    public String getExtension() {
        if (this.isDirectory()) {
            return "";
        }

        int dotIndex = this.getFullName().lastIndexOf('.');

        return dotIndex <= 0 ? "" : this.getFullName().substring(dotIndex);
    }

    @Override
    public IScriptFile resolve(String path) {
        return new ScriptFile(this.path.resolve(path));
    }

    @Override
    public boolean isFile() {
        return this.path.toFile().isFile();
    }

    @Override
    public boolean isDirectory() {
        return this.path.toFile().isDirectory();
    }

    @Override
    public void write(String content) throws IOException {
        this.createParentDirectories();
        Files.write(this.path, content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void append(String content) throws IOException {
        this.createParentDirectories();
        Files.write(this.path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Override
    public String read() throws IOException {
        return String.join("\n", Files.readAllLines(this.path, StandardCharsets.UTF_8));
    }

    @Override
    public void clear() throws IOException {
        Files.write(this.path, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void createDirectory() throws IOException {
        try {
            Files.createDirectories(this.path);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void createFile() throws IOException {
        this.createParentDirectories();
        Files.createFile(this.path);
    }

    @Override
    public void copy(IScriptFile path) throws IOException {
        Files.copy(this.path, path.getPath());
    }

    @Override
    public void delete() throws IOException {
        Files.delete(this.path);
    }

    @Override
    public void move(IScriptFile path) throws IOException {
        Files.move(this.path, path.getPath());
    }

    @Override
    public List<IScriptFile> list() throws IOException {
        return Files.list(this.path).map(ScriptFile::new).collect(Collectors.toList());
    }

    private void createParentDirectories() throws IOException {
        Path parent = this.path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
