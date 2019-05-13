package com.codescrubs.rickandmortyapp.ui.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.codescrubs.rickandmortyapp.R
import com.codescrubs.rickandmortyapp.domain.Character
import kotlinx.android.synthetic.main.item_character.*
import kotlinx.android.extensions.LayoutContainer

class CharacterListAdapter(private val characters: MutableList<Character>, private val itemListener: ItemListener) :
    RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    interface ItemListener {
        fun onItemClick(character: Character)
        fun onFavoriteClick(character: Character)
        fun onUnfavoriteClick(character: Character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return ViewHolder(view, itemListener)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    fun addCharacters(addedCharacters: List<Character>) {
        this.characters.addAll(addedCharacters)
        notifyItemRangeInserted(characters.size - addedCharacters.size, addedCharacters.size)
    }

    fun updateCharacter(character: Character) {
        // TODO: Update Character class so you can use indexOf instead of find
        val oldCharacter = characters.find { it.id == character.id }

        oldCharacter?.let{
            val index = characters.indexOf(oldCharacter)
            characters[index] = character
            notifyItemChanged(index)
        }
    }

    class ViewHolder(override val containerView: View, private val itemListener: ItemListener) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(character: Character) {
            name.text = character.name
            origin.text = character.origin.name
            location.text = character.location.name
            status.text = character.status

            Glide.with(containerView.context).load(character.image).fitCenter().into(image)
            image.contentDescription = character.name


            itemView.setOnClickListener { itemListener.onItemClick(character) }
            favorite.setOnClickListener { if (character.isFavorite) itemListener.onUnfavoriteClick(character) else itemListener.onFavoriteClick(character)}
            setupFavoriteButton(character)
        }

        private fun setupFavoriteButton(character: Character) {
            when (character.isFavorite) {
                true -> favorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        containerView.context,
                        R.drawable.ic_favorite_24dp
                    )
                )
                false -> favorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        containerView.context,
                        R.drawable.ic_favorite_border_24dp
                    )
                )
            }
        }
    }
}