package org.altervista.whovian.client.renderer.gui;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiWidget;
import codechicken.core.gui.IGuiActionListener;
import codechicken.lib.gui.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class CBcontainerGui extends GuiContainer implements IGuiActionListener {

	public ArrayList<GuiWidget> widgets = new ArrayList<GuiWidget>();
    public int xSize, ySize, guiTop, guiLeft;
    
    public CBcontainerGui(Container container)
    {
        this(container, 176, 166);
    }
    
    public CBcontainerGui(Container container, int xSize, int ySize)
    {
        super(container);
        this.xSize = xSize;
        this.ySize = ySize;
    }
    
    @Override
    public void initGui()
    {
    	super.initGui();
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }
    
    public void reset()
    {
        initGui();
        widgets.clear();
        addWidgets();
    }
    
    @Override
    public void setWorldAndResolution(Minecraft mc, int i, int j)
    {
        boolean init = this.mc == null;
        super.setWorldAndResolution(mc, i, j);
        if(init)
            addWidgets();
    }
    
    public void add(GuiWidget widget)
    {
        widgets.add(widget);
        widget.onAdded(this);
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        for(GuiWidget widget : widgets)
            widget.mouseClicked(x, y, button);
    }
    
    @Override
    protected void mouseMovedOrUp(int x, int y, int button)
    {
        super.mouseMovedOrUp(x, y, button);
        for(GuiWidget widget : widgets)
            widget.mouseMovedOrUp(x-guiLeft, y-guiTop, button);
    }
    
    @Override
    protected void mouseClickMove(int x, int y, int button, long time)
    {
        super.mouseClickMove(x, y, button, time);
        for(GuiWidget widget : widgets)
            widget.mouseDragged(x-guiLeft, y-guiTop, button, time);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if(mc.currentScreen == this)
            for(GuiWidget widget : widgets)
                widget.update();
    }
    
    @Override
    public void keyTyped(char c, int keycode)
    {
        super.keyTyped(c, keycode);
        for(GuiWidget widget : widgets)
            widget.keyTyped(c, keycode);        
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if(i != 0)
        {
            Point p = GuiDraw.getMousePosition();
            int scroll = i > 0 ? 1 : -1;
            for(GuiWidget widget : widgets)
                widget.mouseScrolled(p.x, p.y, scroll);
        }
    }

    @Override
    public void actionPerformed(String ident, Object... params)
    {
    }

    public void addWidgets()
    {
    }

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int mousex, int mousey) {
		for(GuiWidget widget : widgets) {
	        widget.draw(mousex, mousey, f);
		}
	}

}
