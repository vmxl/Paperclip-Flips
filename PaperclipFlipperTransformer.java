// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.tweaker;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Collection;
import org.objectweb.asm.ClassWriter;
import org.apache.commons.lang3.mutable.MutableInt;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;
import me.paperclip.paperclipflipper.Reference;
import me.paperclip.paperclipflipper.tweaker.transformers.GuiContainerTransformer;
import com.google.common.collect.ArrayListMultimap;
import me.paperclip.paperclipflipper.tweaker.transformers.ITransformer;
import com.google.common.collect.Multimap;
import net.minecraft.launchwrapper.IClassTransformer;

public class PaperclipFlipperTransformer implements IClassTransformer
{
    private final Multimap<String, ITransformer> transformerMap;
    
    public PaperclipFlipperTransformer() {
        this.transformerMap = (Multimap<String, ITransformer>)ArrayListMultimap.create();
        this.registerTransformer(new GuiContainerTransformer());
    }
    
    private void registerTransformer(final ITransformer transformer) {
        for (final String cls : transformer.getClassName()) {
            this.transformerMap.put((Object)cls, (Object)transformer);
        }
    }
    
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final Collection<ITransformer> transformers = (Collection<ITransformer>)this.transformerMap.get((Object)transformedName);
        if (transformers.isEmpty()) {
            return bytes;
        }
        Reference.logger.info("Found {} transformers for {}", new Object[] { transformers.size(), transformedName });
        final ClassReader reader = new ClassReader(bytes);
        final ClassNode node = new ClassNode();
        reader.accept((ClassVisitor)node, 8);
        final MutableInt classWriterFlags = new MutableInt(3);
        final ClassNode classNode;
        transformers.forEach(transformer -> {
            Reference.logger.info("Applying transformer {} on {}...", new Object[] { transformer.getClass().getName(), transformedName });
            transformer.transform(classNode, transformedName);
            return;
        });
        final ClassWriter writer = new ClassWriter((int)classWriterFlags.getValue());
        try {
            node.accept((ClassVisitor)writer);
        }
        catch (Throwable t) {
            Reference.logger.error("Exception when transforming " + transformedName + " : " + t.getClass().getSimpleName());
            t.printStackTrace();
            this.outputBytecode(transformedName, writer);
            return bytes;
        }
        this.outputBytecode(transformedName, writer);
        return writer.toByteArray();
    }
    
    private void outputBytecode(final String transformedName, final ClassWriter writer) {
        try {
            final File bytecodeDirectory = new File("bytecode");
            final File bytecodeOutput = new File(bytecodeDirectory, transformedName + ".class");
            if (!bytecodeDirectory.exists()) {
                return;
            }
            if (!bytecodeOutput.exists()) {
                bytecodeOutput.createNewFile();
            }
            final FileOutputStream os = new FileOutputStream(bytecodeOutput);
            os.write(writer.toByteArray());
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static {
        PreTransformationChecks.runChecks();
    }
}
