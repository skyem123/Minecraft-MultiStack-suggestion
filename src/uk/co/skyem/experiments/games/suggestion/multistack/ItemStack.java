/*
    Written in 2016 by Skye skyem@hotmail.co.uk

    To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
    This software is distributed without any warranty.

    You should have received a copy of the CC0 Public Domain Dedication along with this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>. 
 */

package uk.co.skyem.experiments.games.suggestion.multistack;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStack {
	// Yep, internal class! Not sure if this should be private or public
	private static class SubStack {
		// Yes, I'm being lazy, this is a proof of concept, not a work of art.
		
		private int amount;
		private Item item;
		private int damage;
		private Metadata metadata;
		
		private SubStack(Item item, int amount, int damage, Metadata metadata) {
			this.item = item;
			this.amount = amount;
			this.damage = damage;
		}
		private SubStack(Item item, int amount, int damage) {
			this(item, amount, damage, null);
		}
		
		// Yeah... Getters and Setters, this is Java, in its unoptimised glory!
		private int getAmount() { return amount; }
		private void setAmount(int amount) { this.amount = amount; }
		private Item getItem() { return item; }
		private void setItem(Item item) { this.item = item; }
		private int getDamage() { return damage; }
		private void setDamage(int damage) { this.damage = damage; }
		public Metadata getMetadata() { return metadata; }
		public void setMetadata(Metadata metadata) { this.metadata = metadata; }
		
	}
	
	
	
	public ItemStack(Item item, int amount, int damage, Metadata metadata) {
		this(new SubStack(item, amount, damage, metadata));
	}
	public ItemStack(Item item, int amount, int damage) {
		this(new SubStack(item, amount, damage));
	}
	public ItemStack() {
		this(new ArrayList<>());
	}
	
	// Internal stuff
	private ItemStack(SubStack subStack) {
		this(new ArrayList<>(Collections.singletonList(subStack)));
	}
	private ItemStack(ArrayList<SubStack> subStacks) {
		this.subStacks = subStacks;
	}
	
	
	// There's probably a better datastructure to use here.
	private ArrayList<SubStack> subStacks;
	
	
	// (for internal use only?) Get the top substack
	private SubStack peekSubStackInternal() { return subStacks.get(subStacks.size()); }
	
	// Get the first / top item in the stack
	public Item peekItem() { return peekSubStackInternal().getItem(); }
	// Get the damage of the first / top item in the stack
	public int peekDamage() { return peekSubStackInternal().getDamage(); }
	// Get the metadata of the first / top item in the stack
	public Metadata peekMetadata() { return peekSubStackInternal().getMetadata(); }
	// Remove the first / top item from the stack
	public Item popItem() {
		int topPos = subStacks.size();
		SubStack top = subStacks.get(topPos);
		Item item = top.getItem();
		top.setAmount(top.getAmount() - 1);
		if (top.getAmount() <= 0 ) { subStacks.remove(topPos); }
		
		return item;
	}
	
	// Get the total amount of items in the stack
	public int getAmount() {
		int amount = 0;
		for (SubStack subStack : subStacks) {
			amount += subStack.getAmount();
		}
		return amount;
	}
	
	// Gets the amount of substacks
	public int getSubStackCount() { return subStacks.size(); }
	
	// Gets the amount of items in a certain stubstack
	public int getSubStackAmount(int subStackPos) { return subStacks.get(subStackPos).getAmount(); }
	
	// Get a list representing the order of substacks
	public List<Item> getSubStackOrder() {
		ArrayList<Item> order = new ArrayList<>();
		for (SubStack subStack : subStacks) {
			order.add(subStack.item);
		}
		
		return Collections.unmodifiableList(order);
	}
	
	// Move around substacks within a stack
	public void swapSubStacks(int a, int b) { Collections.swap(subStacks, a, b); }
	
	public ItemStack getSubStack(int pos) { return new ItemStack(subStacks.get(pos)); }
	
	public ItemStack removeSubStack(int pos) { return new ItemStack(subStacks.remove(pos)); }
	
	public void mergeStack(ItemStack stack, int pos) { subStacks.addAll(pos, stack.subStacks); }
	
	public void addSubStack(Item item, int amount, int damage, Metadata metadata) { subStacks.add(new SubStack(item, amount, damage, metadata)); }
	public void addSubStack(Item item, int amount, int damage) { subStacks.add(new SubStack(item, amount, damage)); }
	
	public void addSubStack(int pos, Item item, int amount, int damage, Metadata metadata) { subStacks.add(pos, new SubStack(item, amount, damage, metadata)); }
	public void addSubStack(int pos, Item item, int amount, int damage) { subStacks.add(pos, new SubStack(item, amount, damage)); }
}
