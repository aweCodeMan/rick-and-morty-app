package com.codescrubs.rickandmortyapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import kotlinx.android.synthetic.main.item_character.*
import kotlinx.android.extensions.LayoutContainer

class CharacterListAdapter(private val characters: List<Character>, private val itemClick: (Character) -> Unit) :
    RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    class ViewHolder(override val containerView: View, private val itemClick: (Character) -> Unit) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(character: Character) {
            name.text = character.name
            origin.text = character.origin.name
            status.text = character.status

            Glide.with(containerView.context).load(character.image).fitCenter().into(image)
            image.contentDescription = character.name

            itemView.setOnClickListener { itemClick(character) }
        }
    }
}